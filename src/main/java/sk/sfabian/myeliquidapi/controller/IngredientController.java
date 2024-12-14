package sk.sfabian.myeliquidapi.controller;

import org.springframework.web.bind.annotation.*;
import sk.sfabian.myeliquidapi.model.Ingredient;
import sk.sfabian.myeliquidapi.model.dto.IngredientDto;
import sk.sfabian.myeliquidapi.service.IngredientService;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDto> fetchIngredients() {
        return ingredientService.fetchAllIngredients();
    }

    @PostMapping
    public IngredientDto addIngredient(@RequestBody IngredientDto ingredient) {
        return ingredientService.addIngredient(new IngredientDto());
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable String id) {
        ingredientService.deleteIngredient(id);
    }
}
