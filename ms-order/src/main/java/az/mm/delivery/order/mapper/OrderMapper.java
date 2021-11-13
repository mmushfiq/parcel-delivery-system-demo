package az.mm.delivery.order.mapper;

import az.mm.delivery.order.domain.Order;
import az.mm.delivery.order.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDto, Order> {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    default Order fromId(Long id) {
        if (id == null) return null;
        Order user = new Order();
        user.setId(id);
        return user;
    }

}