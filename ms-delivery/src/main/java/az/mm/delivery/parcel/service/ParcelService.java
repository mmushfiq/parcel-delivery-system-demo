package az.mm.delivery.parcel.service;

import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.common.security.util.SecurityUtil;
import az.mm.delivery.parcel.domain.Parcel;
import az.mm.delivery.parcel.dto.ParcelDto;
import az.mm.delivery.parcel.enums.OrderStatus;
import az.mm.delivery.parcel.error.validation.ValidationMessage;
import az.mm.delivery.parcel.mapper.ParcelMapper;
import az.mm.delivery.parcel.messaging.MessageProducer;
import az.mm.delivery.parcel.messaging.event.OrderStatusChangeEvent;
import az.mm.delivery.parcel.messaging.event.ParcelOrderEvent;
import az.mm.delivery.parcel.model.ChangeOrderStatusRequest;
import az.mm.delivery.parcel.repository.ParcelRepository;
import az.mm.delivery.parcel.util.ParcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static az.mm.delivery.common.security.util.SecurityUtil.getCurrentUserLogin;

@Service
@Transactional
@RequiredArgsConstructor
public class ParcelService {

    private final ParcelRepository parcelRepository;
    private final ParcelMapper parcelMapper;
    private final MessageProducer messageProducer;

    public ParcelDto save(ParcelDto parcelDto) {
        Parcel parcel = parcelMapper.toEntity(parcelDto);
        parcel = parcelRepository.save(parcel);
        return parcelMapper.toDto(parcel);
    }

    @Transactional(readOnly = true)
    public List<ParcelDto> findAll() {
        List<Parcel> parcelList = SecurityUtil.hasAdminRole() ? parcelRepository.findAll() :
                parcelRepository.findAllByCourier(getCurrentUserLogin());
        return parcelList.stream()
                .map(parcelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ParcelDto findOne(Long id) {
        Optional<Parcel> parcel = SecurityUtil.hasAdminRole() ? parcelRepository.findById(id) :
                parcelRepository.findByIdAndCourier(id, getCurrentUserLogin());
        return parcel.map(parcelMapper::toDto)
                .orElseThrow(() -> InvalidInputException.of(ValidationMessage.PARCEL_NOT_FOUND, List.of(id)));
    }

    public void processParcelOrderEvent(ParcelOrderEvent event) {
        ParcelDto parcelDto = ParcelUtil.createParcelDto(event);
        save(parcelDto);
    }

    public ParcelDto receiveParcelByCourier(Long id) {
        ParcelDto parcelDto = findOne(id);
        parcelDto.setStatus(OrderStatus.IN_PROGRESS);

        OrderStatusChangeEvent event = ParcelUtil.createOrderStatusChangeEvent(parcelDto);
        messageProducer.sendOrderStatusChangeEvent(event);

        return save(parcelDto);
    }

    public String trackParcelOrder(Long id) {
        ParcelDto parcelDto = findOne(id);
        return parcelDto.getCoordination();
    }

    public ParcelDto completeParcelOrder(Long id) {
        ParcelDto parcelDto = findOne(id);
        parcelDto.setStatus(OrderStatus.DELIVERED);

        OrderStatusChangeEvent event = ParcelUtil.createOrderStatusChangeEvent(parcelDto);
        messageProducer.sendOrderStatusChangeEvent(event);

        return save(parcelDto);
    }

    public ParcelDto changeStatus(ChangeOrderStatusRequest request) {
        ParcelDto parcelDto = findOne(request.getOrderId());
        parcelDto.setStatus(request.getNewOrderStatus());
        return save(parcelDto);
    }

}
