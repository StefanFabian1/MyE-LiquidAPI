package sk.sfabian.myeliquidapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
    private String id; // String ID for API compatibility
    private String name;
    private String quantity;
}

