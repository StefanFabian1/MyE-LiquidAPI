package sk.sfabian.myeliquidapi.model;

import lombok.Getter;
import lombok.Setter;
import sk.sfabian.myeliquidapi.model.dto.IngredientDto;

@Setter
@Getter
public class IngredientMovementRequest {
    private IngredientDto ingredient;
    private Movement movement;
}
