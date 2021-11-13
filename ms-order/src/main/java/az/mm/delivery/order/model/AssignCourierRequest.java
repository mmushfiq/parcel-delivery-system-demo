package az.mm.delivery.order.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AssignCourierRequest {

    @NotNull
    private Long orderId;

    @NotBlank
    private String courierUsername;

}
