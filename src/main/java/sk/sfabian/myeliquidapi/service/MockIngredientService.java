package sk.sfabian.myeliquidapi.service;

import org.springframework.stereotype.Service;
import sk.sfabian.myeliquidapi.model.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MockIngredientService {
    private final Map<Integer, Ingredient> ingredientStore = new ConcurrentHashMap<>();
    private int idCounter = 4; // Nasledujúce ID (po inicializovaných dátach)

    public MockIngredientService() {
        // Predvyplnené dáta
        ingredientStore.put(1, new Ingredient(1, "Nicotine", "50ml"));
        ingredientStore.put(2, new Ingredient(2, "VG", "200ml"));
        ingredientStore.put(3, new Ingredient(3, "PG", "150ml"));
    }

    // Fetch all ingredients
    public List<Ingredient> fetchAllIngredients() {
        return new ArrayList<>(ingredientStore.values());
    }

    // Add a new ingredient
    public Ingredient addIngredient(Ingredient ingredient) {
        int id = idCounter++;
        ingredient.setId(id);
        ingredientStore.put(id, ingredient);
        return ingredient;
    }

    // Delete an ingredient by ID
    public void deleteIngredient(Integer id) {
        ingredientStore.remove(id);
    }
}
