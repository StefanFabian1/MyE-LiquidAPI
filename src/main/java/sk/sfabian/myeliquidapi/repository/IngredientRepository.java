package sk.sfabian.myeliquidapi.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import sk.sfabian.myeliquidapi.model.Ingredient;

import java.util.List;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, ObjectId> {

    @Query(value = "{}", fields = "{movements: 0}")
    List<Ingredient> findAllWithoutMovements();
}
