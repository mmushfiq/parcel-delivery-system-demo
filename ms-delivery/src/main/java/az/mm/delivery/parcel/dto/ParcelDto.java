package az.mm.delivery.parcel.dto;

import az.mm.delivery.parcel.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelDto {

    private Long id;

    private String orderNumber;

    private String destination;

    private String coordination;

    private OrderStatus status;

    private String courier;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
