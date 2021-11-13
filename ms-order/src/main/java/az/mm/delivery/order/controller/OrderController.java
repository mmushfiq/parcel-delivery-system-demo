package az.mm.delivery.order.controller;

import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.order.dto.OrderDto;
import az.mm.delivery.order.model.AssignCourierRequest;
import az.mm.delivery.order.model.ChangeOrderDestinationRequest;
import az.mm.delivery.order.model.ChangeOrderStatusRequest;
import az.mm.delivery.order.model.CreateOrderRequest;
import az.mm.delivery.order.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@NotNull @PathVariable Long id) {
        OrderDto orderDto = orderService.findOne(id);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        OrderDto result = orderService.createOrder(createOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody OrderDto orderDto) {
        if (orderDto.getId() == null) {
            throw InvalidInputException.of("Order ID cannot be null");
        }
        OrderDto result = orderService.save(orderDto);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "User can change order destination before accepted", response = OrderDto.class)
    @PutMapping("/change-destination")
    public ResponseEntity<OrderDto> changeDestination(@Valid @RequestBody ChangeOrderDestinationRequest request) {
        OrderDto result = orderService.changeDestination(request);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "User can cancel order before accepted")
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelOrder(@NotNull @PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "After admin accept order some new details determine for order", response = OrderDto.class)
    @PutMapping("/accept/{id}")
    public ResponseEntity<OrderDto> acceptOrder(@NotNull @PathVariable Long id) {
        OrderDto result = orderService.acceptOrder(id);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Admin can change order status", response = OrderDto.class)
    @PutMapping("/change-status")
    public ResponseEntity<OrderDto> changeStatus(@Valid @RequestBody ChangeOrderStatusRequest request) {
        OrderDto result = orderService.changeStatus(request);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Admin assign courier for each order", response = OrderDto.class)
    @PutMapping("/assign-courier")
    public ResponseEntity<OrderDto> assignCourier(@Valid @RequestBody AssignCourierRequest request) {
        OrderDto result = orderService.assignCourier(request);
        return ResponseEntity.ok(result);
    }

}
