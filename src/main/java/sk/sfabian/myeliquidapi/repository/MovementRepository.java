package sk.sfabian.myeliquidapi.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import sk.sfabian.myeliquidapi.model.Movement;

import java.util.List;

@Repository
public interface MovementRepository extends MongoRepository<Movement, ObjectId> {
    List<Movement> findByIngredientId(String ingredientId);

    void deleteByIngredientId(String ingredientId);
}
