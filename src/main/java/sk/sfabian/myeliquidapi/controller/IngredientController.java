package sk.sfabian.myeliquidapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.sfabian.myeliquidapi.model.Movement;
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
    public List<IngredientDto> getIngredients() {
        return ingredientService.getIngredientsWithoutMovements();
    }

    @GetMapping("/ingredient/{id}")
    public IngredientDto getIngredientById(@PathVariable String id) {
        return ingredientService.getIngredient(id);
    }

    @GetMapping("/search")
    public List<IngredientDto> searchIngredients(@RequestParam String query) {
        return ingredientService.searchIngredients(query);
    }

    @GetMapping("/{id}/movements")
    public ResponseEntity<List<Movement>> getMovements(@PathVariable String id) {
        List<Movement> movements = ingredientService.getMovements(id);
        return ResponseEntity.ok(movements);
    }

    @PostMapping("/{id}/movements")
    public ResponseEntity<Void> addMovement(
            @PathVariable String id,
            @RequestParam double quantity,
            @RequestParam double totalPrice,
            @RequestParam String type) {
        ingredientService.addMovement(id, quantity, totalPrice, type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ingredient")
    public IngredientDto addIngredient(@RequestBody IngredientDto ingredientDto) {
        return ingredientService.addIngredient(ingredientDto);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable String id) {
        ingredientService.deleteIngredient(id);
    }

    @PutMapping("/ingredient/{id}")
    public ResponseEntity<Void> updateIngredient(
            @PathVariable String id,
            @RequestBody IngredientDto ingredientDto) {
        ingredientService.updateIngredient(id, ingredientDto);
        return ResponseEntity.ok().build();
    }
}
