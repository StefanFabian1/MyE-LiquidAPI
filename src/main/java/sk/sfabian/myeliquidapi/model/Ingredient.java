package sk.sfabian.myeliquidapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ingredients")
public class Ingredient {
    @Id
    private ObjectId id;
    private String name;
    private Double quantity;
    private String unit;
    private Double unitPrice;
    private String category;
    private String subcategory;
    private String description;
    private String brand;
    private List<Movement> movements;
}
