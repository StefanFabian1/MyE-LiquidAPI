package sk.sfabian.myeliquidapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movement {
    @Id
    private ObjectId id;
    private Double quantity;
    private Double totalPrice;
    private String type;
    private Long timestamp;
}
