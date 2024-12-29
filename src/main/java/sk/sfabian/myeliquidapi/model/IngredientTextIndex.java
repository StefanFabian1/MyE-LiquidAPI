package sk.sfabian.myeliquidapi.model;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.stereotype.Component;

@Component
public class IngredientTextIndex {

    private final MongoTemplate mongoTemplate;

    public IngredientTextIndex(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void createTextIndex() {
        TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                .onField("name")
                .onField("description")
                .build();
        mongoTemplate.indexOps("ingredients").ensureIndex(textIndex);
    }
}
