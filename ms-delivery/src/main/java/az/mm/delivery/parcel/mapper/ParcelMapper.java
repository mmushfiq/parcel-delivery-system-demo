package az.mm.delivery.parcel.mapper;

import az.mm.delivery.parcel.domain.Parcel;
import az.mm.delivery.parcel.dto.ParcelDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ParcelMapper extends EntityMapper<ParcelDto, Parcel> {

    ParcelMapper INSTANCE = Mappers.getMapper(ParcelMapper.class);

    default Parcel fromId(Long id) {
        if (id == null) return null;
        Parcel user = new Parcel();
        user.setId(id);
        return user;
    }

}