package hu.fourig.partner.service;

import hu.fourig.partner.dto.AddressDto;
import hu.fourig.partner.entity.Address;
import hu.fourig.partner.entity.Partner;
import hu.fourig.partner.mapper.AddressMapper;
import hu.fourig.partner.repository.AddressRepository;
import hu.fourig.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final PartnerRepository partnerRepository;

    public List<AddressDto> listAddresses(Long partnerId, Optional<String> filter) {
        return partnerRepository.findById(partnerId).orElseThrow().getAddresses().stream()
                .filter(a -> filter.isEmpty() || a.getCity().toUpperCase().contains(filter.get().toUpperCase()) ||
                        a.getStreet().toUpperCase().contains(filter.get().toUpperCase()))
                .map(addressMapper::map).toList();
    }

    public AddressDto getAddress(Long partnerId, Long addressId) {
        return addressMapper.map(partnerRepository.findById(partnerId).orElseThrow().getAddresses().stream()
                .filter(a -> addressId.longValue() == a.getId().longValue()).findAny().orElseThrow());
    }

    public AddressDto createAddress(Long partnerId, AddressDto addressDto) {
        addressDto.setId(null);
        return updateAddress(partnerId, addressDto);
    }

    public AddressDto updateAddress(Long partnerId, AddressDto addressDto) {
        Partner partner = partnerRepository.findById(partnerId).orElseThrow();

        Address address = addressMapper.map(addressDto);
        address.setPartner(partner);

        return addressMapper.map(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(Long partnerId, Long addressId) {
        Partner partner = partnerRepository.findById(partnerId).orElseThrow();
        partner.getAddresses().stream().filter(a -> addressId.longValue() == a.getId().longValue()).findAny()
                .ifPresent(a -> partner.getAddresses().remove(a));
    }
}
