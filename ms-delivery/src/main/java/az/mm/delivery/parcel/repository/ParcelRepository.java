package az.mm.delivery.parcel.repository;

import az.mm.delivery.parcel.domain.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    List<Parcel> findAllByCourier(String courier);

    Optional<Parcel> findByIdAndCourier(Long id, String courier);

}