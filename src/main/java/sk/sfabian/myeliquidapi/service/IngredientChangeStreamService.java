package sk.sfabian.myeliquidapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.mongodb.client.model.Filters.eq;

@Service
public class IngredientChangeStreamService {
    private final MongoTemplate mongoTemplate;
    private final ExecutorService executorService;
    private Consumer<String> eventConsumer;

    @Autowired
    public IngredientChangeStreamService(MongoTemplate mongoTemplate, ExecutorService executorService) {
        this.mongoTemplate = mongoTemplate;
        this.executorService = executorService;
    }
    public void setEventConsumer(Consumer<String> eventConsumer) {
        this.eventConsumer = eventConsumer;
    }
    @PostConstruct
    public void startListening() {
        executorService.submit(() -> {
            try {
                MongoCollection<Document> collection = mongoTemplate.getCollection("ingredients");
                MongoCursor<ChangeStreamDocument<Document>> cursor = collection.watch().iterator();

                while (cursor.hasNext()) {
                    ChangeStreamDocument<Document> event = cursor.next();
                    handleEvent(event);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleEvent(ChangeStreamDocument<Document> event) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String message = switch (event.getOperationType()) {
                case INSERT -> {
                    Document fullDocument = event.getFullDocument();
                    yield "A" + objectMapper.writeValueAsString(fullDocument); // Prefix "A" for ADD
                }
                case UPDATE -> {
                    ObjectId id = event.getDocumentKey().getObjectId("_id").getValue();
                    Document fullDocument = mongoTemplate.getCollection("ingredients")
                            .find(eq("_id", id))
                            .first();
                    yield "U" + objectMapper.writeValueAsString(fullDocument); // Prefix "U" for UPDATE
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
            e.printStackTrace();
        }
    }
}
