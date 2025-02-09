package hu.fourig.partner.mapper;

import hu.fourig.partner.dto.AddressDto;
import hu.fourig.partner.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto map(Address address);
    @Mapping(target = "partner", ignore = true)
    Address map(AddressDto addressDto);
}
