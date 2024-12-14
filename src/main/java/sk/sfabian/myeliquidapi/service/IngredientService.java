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

    public List<IngredientDto> fetchAllIngredients() {
        return ingredientRepository.findAll().stream()
                .map(IngredientMapper::toDto)
                .collect(Collectors.toList());
    }

    public IngredientDto addIngredient(IngredientDto ingredientDto) {
        Ingredient ingredient = IngredientMapper.fromDto(ingredientDto);
        Ingredient savedIngredient = ingredientRepository.save(ingredient);
        return IngredientMapper.toDto(savedIngredient);
    }

    public void deleteIngredient(String id) {

        ingredientRepository.deleteById(new ObjectId(id));
    }
}
