package sk.sfabian.myeliquidapi.service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sk.sfabian.myeliquidapi.model.Ingredient;
import sk.sfabian.myeliquidapi.model.dto.IngredientDto;
import sk.sfabian.myeliquidapi.model.dto.IngredientMapper;
import sk.sfabian.myeliquidapi.repository.IngredientRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
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

        ingredientRepository.deleteById(new ObjectId(id));
    }

    public List<IngredientDto> getIngredientsWithoutMovements() {
        List<Ingredient> ingredients = ingredientRepository.findAllWithoutMovements();
        return ingredients.stream()
                .map(IngredientMapper::toDto)
                .collect(Collectors.toList());
    }
}
