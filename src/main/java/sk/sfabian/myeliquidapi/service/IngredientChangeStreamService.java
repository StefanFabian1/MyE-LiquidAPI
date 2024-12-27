package sk.sfabian.myeliquidapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.stereotype.Service;
import sk.sfabian.myeliquidapi.model.Ingredient;
import sk.sfabian.myeliquidapi.model.dto.IngredientDto;
import sk.sfabian.myeliquidapi.model.dto.IngredientMapper;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Service
public class IngredientChangeStreamService {
    private final MongoTemplate mongoTemplate;
    private final ExecutorService executorService;
    private final MappingMongoConverter mappingMongoConverter;
    @Setter
    private Consumer<String> eventConsumer;

    @Autowired
    public IngredientChangeStreamService(MongoTemplate mongoTemplate, ExecutorService executorService, MappingMongoConverter mappingMongoConverter) {
        this.mongoTemplate = mongoTemplate;
        this.executorService = executorService;
        this.mappingMongoConverter = mappingMongoConverter;
    }

    @PostConstruct
    public void startListening() {
        executorService.submit(() -> {
            try {
                MongoCollection<Document> collection = mongoTemplate.getCollection("ingredients");
                for (ChangeStreamDocument<Document> event : collection.watch()) {
                    handleEvent(event);
                }
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        });
    }

    private void handleEvent(ChangeStreamDocument<Document> event) {
        assert event.getOperationType() != null;
        assert event.getDocumentKey() != null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String message = switch (event.getOperationType()) {
                case INSERT -> {
                    assert event.getFullDocument() != null;
                    Ingredient ingredient = mappingMongoConverter.read(Ingredient.class, event.getFullDocument());
                    yield "A" + objectMapper.writeValueAsString(IngredientMapper.toDto(ingredient));
                }
                case UPDATE -> {
                    ObjectId id = event.getDocumentKey().getObjectId("_id").getValue();
                    Ingredient ingredient = mongoTemplate.findById(id, Ingredient.class, "ingredients");

                    if (ingredient != null) {
                        IngredientDto ingredientDto = IngredientMapper.toDto(ingredient);
                        yield "U" + objectMapper.writeValueAsString(ingredientDto);
                    } else {
                        yield "Update event: Ingredient not found";
                    }
                }
                case DELETE -> {
                    String id = event.getDocumentKey().getObjectId("_id").getValue().toHexString();
                    yield "D{\"_id\":\"" + id + "\"}"; // Prefix "D" for DELETE
                }
                default -> "Unhandled event: " + event.getOperationType();
            };

            if (eventConsumer != null) {
                eventConsumer.accept(message); // Send the message to the consumer
            }
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
