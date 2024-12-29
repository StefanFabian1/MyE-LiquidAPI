package sk.sfabian.myeliquidapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.sfabian.myeliquidapi.model.IngredientTextIndex;

@Component
public class IngredientIndexInitializer implements CommandLineRunner {
    private final IngredientTextIndex ingredientTextIndex;

    public IngredientIndexInitializer(IngredientTextIndex ingredientTextIndex) {
        this.ingredientTextIndex = ingredientTextIndex;
    }

    @Override
    public void run(String... args) {
        ingredientTextIndex.createTextIndex();
    }
}
