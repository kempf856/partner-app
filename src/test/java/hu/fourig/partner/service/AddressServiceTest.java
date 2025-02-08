package hu.fourig.partner.service;

import hu.fourig.partner.dto.AddressDto;
import hu.fourig.partner.entity.Address;
import hu.fourig.partner.entity.Partner;
import hu.fourig.partner.mapper.AddressMapper;
import hu.fourig.partner.mapper.AddressMapperImpl;
import hu.fourig.partner.repository.AddressRepository;
import hu.fourig.partner.repository.PartnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    private static final Long PARTNER_ID = 1L;
    private static final Long ADDRESS_ID = 2L;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Spy
    private AddressMapper addressMapper = new AddressMapperImpl();

    @InjectMocks
    private AddressService addressService;

    @Test
    void listAddresses_WithoutFilter_FullListForPartner() {
        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.of(createPartner()));

        List<AddressDto> result = addressService.listAddresses(PARTNER_ID, Optional.empty());

        assertEquals(2, result.size());

        verify(partnerRepository).findById(PARTNER_ID);
        verify(addressMapper, times(2)).map(any(Address.class));
    }

    @Test
    void listAddresses_WithFilter_FilteredListForPartner() {
        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.of(createPartner()));

        List<AddressDto> result = addressService.listAddresses(PARTNER_ID, Optional.of("bar"));

        assertEquals(1, result.size());

        verify(partnerRepository).findById(PARTNER_ID);
        verify(addressMapper).map(any(Address.class));
    }

    @Test
    void listAddress_InvalidPartnerId_Exception() {
        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> addressService.listAddresses(PARTNER_ID, Optional.empty()));

        verify(partnerRepository).findById(PARTNER_ID);
    }

    @Test
    void getAddress_ValidPartnerAndAddress_Address() {
        Partner partner = createPartner();
        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.of(partner));

        AddressDto result = addressService.getAddress(PARTNER_ID, ADDRESS_ID);

        assertEquals(ADDRESS_ID, result.getId());

        verify(partnerRepository).findById(PARTNER_ID);
        verify(addressMapper).map(partner.getAddresses().get(0));
    }

    @Test
    void getAddress_InvalidPartnerId_Exception() {
        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> addressService.getAddress(PARTNER_ID, ADDRESS_ID));

        verify(partnerRepository).findById(PARTNER_ID);
    }

    @Test
    void getAddress_InvalidAddressId_Exception() {
        Partner partner = createPartner();
        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.of(partner));

        assertThrows(NoSuchElementException.class, () -> addressService.getAddress(PARTNER_ID, 4L));

        verify(partnerRepository).findById(PARTNER_ID);
    }

    @Test
    void createAddress_ValidPartnerAndAddress_NewAddress() {
        AddressDto addressDto = AddressDto.builder().city("Alma").build();
        Address address = Address.builder().id(ADDRESS_ID).city(addressDto.getCity()).build();

        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.of(Partner.builder().id(PARTNER_ID).build()));
        when(addressRepository.save(any())).thenReturn(address);

        AddressDto result = addressService.createAddress(PARTNER_ID, addressDto);

        assertEquals(ADDRESS_ID, result.getId());
        assertEquals(addressDto.getCity(), result.getCity());

        verify(partnerRepository).findById(PARTNER_ID);
        verify(addressRepository).save(any());
        verify(addressMapper).map(addressDto);
        verify(addressMapper).map(address);
    }

    @Test
    void createAddress_InvalidPartner_Exception() {
        AddressDto addressDto = AddressDto.builder().city("Alma").build();

        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> addressService.createAddress(PARTNER_ID, addressDto));

        verify(partnerRepository).findById(PARTNER_ID);
    }

    @Test
    void deleteAddress_ValidPartnerAndAddress_Success() {
        Partner partner = createPartner();

        when(partnerRepository.findById(PARTNER_ID)).thenReturn(Optional.of(partner));

        addressService.deleteAddress(PARTNER_ID, ADDRESS_ID);

        assertEquals(1, partner.getAddresses().size());

        verify(partnerRepository).findById(PARTNER_ID);
    }

    private Partner createPartner() {
        return Partner.builder()
                .id(PARTNER_ID)
                .addresses(new ArrayList<>(List.of(Address.builder().id(ADDRESS_ID).city("Alma").street("Körte").build(),
                        Address.builder().id(3L).city("Barack").street("Meggy").build())))
                .build();
    }
}
