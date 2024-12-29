package sk.sfabian.myeliquidapi.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sk.sfabian.myeliquidapi.model.Ingredient;
import sk.sfabian.myeliquidapi.model.Movement;
import sk.sfabian.myeliquidapi.model.dto.IngredientDto;
import sk.sfabian.myeliquidapi.model.dto.IngredientMapper;
import sk.sfabian.myeliquidapi.repository.IngredientRepository;
import sk.sfabian.myeliquidapi.repository.MovementRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final MovementRepository movementRepository;

    public IngredientService(IngredientRepository ingredientRepository, MovementRepository movementRepository) {
        this.ingredientRepository = ingredientRepository;
        this.movementRepository = movementRepository;
    }

    /* TODO --- Toto by teoreticky malo vratit ingrediencie aj s movementmi, ale toto FE zatial nepotrebuje, nedrzi movements v cahce
    TODO query je jednoduche, ale potrebovali by sme result zabalit do noveho DTO a tento DTO vytvorit aj na FE
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(IngredientMapper::toDto)
                .collect(Collectors.toList());
    }
    */

    public IngredientDto addIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = IngredientMapper.fromDto(ingredientDto);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return IngredientMapper.toDto(savedIngredient);
    }

    public void deleteIngredient(String id) {
        movementRepository.deleteByIngredientId(id);
        ingredientRepository.deleteById(new ObjectId(id));
    }

    public List<IngredientDto> getIngredientsWithoutMovements() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream()
                .map(IngredientMapper::toDto)
                .collect(Collectors.toList());
    }

    public IngredientDto getIngredient(String id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(new ObjectId(id));
        return ingredient.map(IngredientMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found for id: " + id));
    }

    public void updateIngredient(String id, IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientRepository.findById(new ObjectId(ingredientDto.getMongoId()))
                .orElseThrow(() -> new IllegalArgumentException("Ingredient not found with id: " + id));

        ingredient.setName(ingredientDto.getName());
        ingredient.setQuantity(ingredientDto.getQuantity());
        ingredient.setUnit(ingredientDto.getUnit());
        ingredient.setUnitPrice(ingredientDto.getUnitPrice());
        ingredient.setCategory(ingredientDto.getCategory());
        ingredient.setSubcategory(ingredientDto.getSubcategory());
        ingredient.setBrand(ingredientDto.getBrand());
        ingredient.setDescription(ingredientDto.getDescription());

        ingredientRepository.save(ingredient);
    }

    public void addMovement(String ingredientId, double quantity, double totalPrice, String type) {
        // Vytvorenie pohybu
        Movement movement = new Movement();
        movement.setIngredientId(ingredientId);
        movement.setQuantity(quantity);
        movement.setTotalPrice(totalPrice);
        movement.setType(type);
        movement.setTimestamp(System.currentTimeMillis());
        movementRepository.save(movement);

        // Aktualizácia ingrediencie
        Ingredient ingredient = ingredientRepository.findById(new ObjectId(ingredientId))
                .orElseThrow(() -> new RuntimeException("Ingrediencia neexistuje!"));
        double newQuantity = ingredient.getQuantity() + quantity;
        double newPrice = newQuantity > 0 ? totalPrice / newQuantity : 0.0;

        ingredient.setQuantity(newQuantity);
        ingredient.setUnitPrice(newPrice);
        ingredientRepository.save(ingredient);
    }

    public List<Movement> getMovements(String ingredientId) {
        return movementRepository.findByIngredientId(ingredientId);
    }

    public List<IngredientDto> searchIngredients(String query) {
        List<Ingredient> ingredients = ingredientRepository.searchIngredients(query);

        if (ingredients.isEmpty()) {
            // Ak textový index nič nenájde, použite regex
            ingredients = ingredientRepository.searchIngredientsByRegex(query);
        }

        return ingredients.stream()
                .map(IngredientMapper::toDto)
                .collect(Collectors.toList());
    }
}
