package sk.sfabian.myeliquidapi.model.dto;

import org.bson.types.ObjectId;
import sk.sfabian.myeliquidapi.model.Ingredient;

import java.util.ArrayList;

public class IngredientMapper {
        public static IngredientDto toDto(Ingredient ingredient) {
            return new IngredientDto(
                    ingredient.getId() != null ? ingredient.getId().toHexString() : null,
                    ingredient.getName(),
                    ingredient.getQuantity(),
                    ingredient.getUnit(),
                    ingredient.getUnitPrice(),
                    ingredient.getCategory(),
                    ingredient.getSubcategory(),
                    ingredient.getDescription(),
                    ingredient.getBrand()
            );
        }

        public static Ingredient fromDto(IngredientDto dto) {
            //TODO pridanie novej
            return new Ingredient(
            );
        }
}
