package sk.sfabian.myeliquidapi.controller;

import org.springframework.web.bind.annotation.*;
import sk.sfabian.myeliquidapi.model.Ingredient;
import sk.sfabian.myeliquidapi.service.IngredientService;
import sk.sfabian.myeliquidapi.service.MockIngredientService;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final MockIngredientService ingredientService;

    public IngredientController(MockIngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<Ingredient> fetchIngredients() {
        return ingredientService.fetchAllIngredients();
    }

    @PostMapping
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        return ingredientService.addIngredient(ingredient);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable Integer id) {
        ingredientService.deleteIngredient(id);
    }
}
