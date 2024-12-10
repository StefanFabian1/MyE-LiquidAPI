package sk.sfabian.myeliquidapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sk.sfabian.myeliquidapi.model.Ingredient;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, Integer> {
    // Možno neskôr pridáme vlastné metódy (napr. findByName)
}
