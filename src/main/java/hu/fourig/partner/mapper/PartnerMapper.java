package hu.fourig.partner.mapper;

import hu.fourig.partner.dto.PartnerDto;
import hu.fourig.partner.entity.Partner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface PartnerMapper {

    @Mapping(target = "addresses", ignore = true)
    Partner map(PartnerDto partnerDto);
    PartnerDto map(Partner partner);
}
