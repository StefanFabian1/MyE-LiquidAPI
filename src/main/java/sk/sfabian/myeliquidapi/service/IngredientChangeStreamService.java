package sk.sfabian.myeliquidapi.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import sk.sfabian.myeliquidapi.controller.IngredientStreamController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

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
        String message = switch (event.getOperationType()) {
            case INSERT -> "Added ingredient: " + event.getFullDocument().toJson();
            case UPDATE -> "Updated fields: " + event.getUpdateDescription().getUpdatedFields();
            case DELETE -> "Deleted ingredient with ID: " + event.getDocumentKey().getObjectId("_id");
            default -> "Unhandled event: " + event.getOperationType();
        };
        if (eventConsumer != null) {
            eventConsumer.accept(message);
        }
    }
}
