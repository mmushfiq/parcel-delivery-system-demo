package az.mm.delivery.order.service;

import az.mm.delivery.common.enums.UserType;
import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.order.domain.Order;
import az.mm.delivery.order.dto.OrderDto;
import az.mm.delivery.order.enums.NotificationMessage;
import az.mm.delivery.order.enums.OrderStatus;
import az.mm.delivery.order.error.exception.CourierMustNotBeAssignedException;
import az.mm.delivery.order.error.exception.OrderCanNotCancelException;
import az.mm.delivery.order.error.validation.ValidationMessage;
import az.mm.delivery.order.mapper.OrderMapper;
import az.mm.delivery.order.messaging.MessageProducer;
import az.mm.delivery.order.messaging.event.EmailNotificationEvent;
import az.mm.delivery.order.messaging.event.OrderStatusChangeEvent;
import az.mm.delivery.order.messaging.event.ParcelOrderEvent;
import az.mm.delivery.order.model.AssignCourierRequest;
import az.mm.delivery.order.model.ChangeOrderDestinationRequest;
import az.mm.delivery.order.model.ChangeOrderStatusRequest;
import az.mm.delivery.order.model.CreateOrderRequest;
import az.mm.delivery.order.repository.OrderRepository;
import az.mm.delivery.order.util.OrderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static az.mm.delivery.common.security.util.SecurityUtil.getCurrentUserLogin;
import static az.mm.delivery.common.security.util.SecurityUtil.getCurrentUserType;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final MessageProducer messageProducer;

    public OrderDto save(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        List<Order> orderList = isAdmin() ? orderRepository.findAll() :
                orderRepository.findAllByCreatedBy(getCurrentUserLogin());
        return orderList.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Transactional(readOnly = true)
    public OrderDto findOne(Long id) {
        Optional<Order> order = isAdmin() ? orderRepository.findById(id) :
                orderRepository.findByIdAndCreatedBy(id, getCurrentUserLogin());
        return order.map(orderMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.ORDER_NOT_FOUND, List.of(id)));
    }

    @Transactional(readOnly = true)
    public OrderDto findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(orderMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.ORDER_NOT_FOUND, List.of(orderNumber)));
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public OrderDto createOrder(CreateOrderRequest req) {
        OrderDto orderDto = OrderDto.builder()
                .name(req.getName())
                .destination(req.getDestination())
                .note(req.getNote())
                .orderNumber(UUID.randomUUID().toString())
                .status(OrderStatus.INITIAL)
                .createdBy(getCurrentUserLogin())
                .build();
        return save(orderDto);
    }

    public OrderDto changeDestination(ChangeOrderDestinationRequest request) {
        OrderDto orderDto = findOne(request.getOrderId());
        orderDto.setDestination(request.getNewDestination());
        return save(orderDto);
    }

    public void cancelOrder(Long id) {
        OrderDto orderDto = findOne(id);
        if (orderDto.getStatus().ordinal() > OrderStatus.PENDING.ordinal()) {
            throw new OrderCanNotCancelException();
        }
        orderDto.setStatus(OrderStatus.CANCEL);
        save(orderDto);
    }

    public OrderDto acceptOrder(Long id) {
        OrderDto orderDto = findOne(id);
        if (orderDto.getStatus() == OrderStatus.ACCEPTED) {
            return orderDto;
        }
        double weight = OrderUtil.calculateRandomWeight();
        orderDto.setWeight(weight);
        orderDto.setAmount(OrderUtil.calculateTotalDeliveryAmount(weight));
        orderDto.setStatus(OrderStatus.ACCEPTED);
        orderDto = save(orderDto);

        EmailNotificationEvent event =
                OrderUtil.createEmailNotificationEvent(orderDto, NotificationMessage.ORDER_ACCEPTED);
        messageProducer.sendEmailNotificationEvent(event);

        return orderDto;
    }

    public OrderDto changeStatus(ChangeOrderStatusRequest request) {
        OrderDto orderDto = findOne(request.getOrderId());
        orderDto.setStatus(request.getNewOrderStatus());
        return save(orderDto);
    }

    public OrderDto changeStatus(OrderStatusChangeEvent event) {
        OrderDto orderDto = findByOrderNumber(event.getOrderNumber());
        orderDto.setStatus(event.getOrderStatus());
        return save(orderDto);
    }

    public OrderDto assignCourier(AssignCourierRequest request) {
        OrderDto orderDto = findOne(request.getOrderId());
        checkOrderStatus(orderDto);

        orderDto.setCourier(request.getCourierUsername());
        orderDto.setStatus(OrderStatus.PENDING);
        orderDto = save(orderDto);

        ParcelOrderEvent event = OrderUtil.createParcelOrderEvent(orderDto);
        messageProducer.sendParcelOrderEvent(event);

        return orderDto;
    }

    private void checkOrderStatus(OrderDto orderDto) {
        OrderStatus status = orderDto.getStatus();
        if (status == OrderStatus.INITIAL || status == OrderStatus.CANCEL) {
            throw new CourierMustNotBeAssignedException();
        }
    }

    private boolean isAdmin() {
        return getCurrentUserType() == UserType.ADMIN;
    }

}
