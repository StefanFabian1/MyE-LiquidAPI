package sk.sfabian.myeliquidapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.sfabian.myeliquidapi.model.Movement;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
    private String mongoId;
    private String name;
    private Double quantity;
    private String unit;
    private Double unitPrice;
    private String category;
    private String subcategory;
    private String description;
    private String brand;
}

