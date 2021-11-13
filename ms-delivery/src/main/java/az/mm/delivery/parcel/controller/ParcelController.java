package az.mm.delivery.parcel.controller;

import az.mm.delivery.common.error.exception.InvalidInputException;
import az.mm.delivery.parcel.dto.ParcelDto;
import az.mm.delivery.parcel.model.ChangeOrderStatusRequest;
import az.mm.delivery.parcel.service.ParcelService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/parcels")
@RequiredArgsConstructor
@Validated
public class ParcelController {

    private final ParcelService parcelService;

    @ApiOperation(value = "All parcels display here after admin assign couriers to them", response = ParcelDto.class)
    @GetMapping
    public List<ParcelDto> getAllParcels() {
        return parcelService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParcelDto> getParcel(@NotNull @PathVariable Long id) {
        ParcelDto parcelDto = parcelService.findOne(id);
        return ResponseEntity.ok(parcelDto);
    }

    @PutMapping
    public ResponseEntity<ParcelDto> updateParcel(@Valid @RequestBody ParcelDto parcelDto) {
        if (parcelDto.getId() == null) {
            throw InvalidInputException.of("Parcel ID cannot be null");
        }
        ParcelDto result = parcelService.save(parcelDto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/change-status")
    public ResponseEntity<ParcelDto> changeStatus(@Valid @RequestBody ChangeOrderStatusRequest request) {
        ParcelDto result = parcelService.changeStatus(request);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Couriers receive parcels which assign them. " +
            "Then order status change from PENDING to IN_PROGRESS", response = ParcelDto.class)
    @PutMapping("/receive-parcel/{id}")
    public ResponseEntity<ParcelDto> receiveParcelByCourier(@NotNull @PathVariable Long id) {
        ParcelDto result = parcelService.receiveParcelByCourier(id);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Admins can track each delivery order with their id", response = String.class)
    @GetMapping("/track/{id}")
    public ResponseEntity<String> trackParcelOrder(@NotNull @PathVariable Long id) {
        String coordination = parcelService.trackParcelOrder(id);
        return ResponseEntity.ok(coordination);
    }

    @ApiOperation(value = "Courier complete order if parcel delivered successfully", response = ParcelDto.class)
    @PutMapping("/complete-order/{id}")
    public ResponseEntity<ParcelDto> completeParcelOrder(@NotNull @PathVariable Long id) {
        ParcelDto result = parcelService.completeParcelOrder(id);
        return ResponseEntity.ok(result);
    }

}
