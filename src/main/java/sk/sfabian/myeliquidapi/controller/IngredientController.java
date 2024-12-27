package sk.sfabian.myeliquidapi.controller;

import org.springframework.web.bind.annotation.*;
import sk.sfabian.myeliquidapi.model.IngredientMovementRequest;
import sk.sfabian.myeliquidapi.model.dto.IngredientDto;
import sk.sfabian.myeliquidapi.service.IngredientService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDto> getIngredients() {
        return ingredientService.getIngredientsWithoutMovements();
    }

    @GetMapping("/ingredient/{name}")
    public IngredientDto getIngredientByName(@PathVariable String name) {
        // TODO
        //return ingredientService.getIngredientByName(name);
        return new IngredientDto();
    }

    @GetMapping("/ingredient/{id}/movements")
    public IngredientDto getIngredientMovements(@PathVariable String id) {
        // TODO
        //return ingredientService.getIngredientByName(name);
        return new IngredientDto();
    }

    @PostMapping("/ingredients/movement")
    public String addIngredientMovement(@RequestBody IngredientMovementRequest request) {

        IngredientDto ingredient = request.getIngredient();

        ingredientService.addIngredient(ingredient); // Napr. pridanie ingrediencie
        //movementService.addMovement(movement); // Napr. pridanie pohybu

        return "Ingredient and movement added successfully";
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable String id) {
        ingredientService.deleteIngredient(id);
    }
}
