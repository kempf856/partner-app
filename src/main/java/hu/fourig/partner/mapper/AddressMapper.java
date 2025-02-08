package hu.fourig.partner.mapper;

import hu.fourig.partner.dto.AddressDto;
import hu.fourig.partner.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto map(Address address);
    Address map(AddressDto addressDto);
}
