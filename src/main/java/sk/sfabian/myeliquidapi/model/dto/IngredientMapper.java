package sk.sfabian.myeliquidapi.model.dto;

import org.bson.types.ObjectId;
import sk.sfabian.myeliquidapi.model.Ingredient;

public class IngredientMapper {
        public static IngredientDto toDto(Ingredient ingredient) {
            return new IngredientDto(
                    ingredient.getId() != null ? ingredient.getId().toHexString() : null,
                    ingredient.getName(),
                    ingredient.getQuantity()
            );
        }

        public static Ingredient fromDto(IngredientDto dto) {
            return new Ingredient(
                    dto.getId() != null ? new ObjectId(dto.getId()) : null,
                    dto.getName(),
                    dto.getQuantity()
            );
        }
}
