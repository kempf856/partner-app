package hu.fourig.partner.repository;

import hu.fourig.partner.entity.Address;
import hu.fourig.partner.entity.Partner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PartnerRepositoryTest {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void init() {
        partnerRepository.save(Partner.builder().name("p1").email("vadAlmafa@utca.hu").build());
        partnerRepository.save(Partner.builder().name("p2").email("e2").build());
        Partner partner3 = partnerRepository.save(Partner.builder().name("p3").email("e3").build());

        addressRepository.save(Address.builder().city("nagyváros").street("hatalMasok útja 2.").partner(partner3).build());
    }

    @Test
    void findByFilter_ValidFilter_TwoPartners() {
        List<Partner> result = partnerRepository.findByFilter("alma".toUpperCase());

        assertEquals(2, result.size());
    }

    @Test
    void findByFilter_InvalidFilter_ZeroPartner() {
        List<Partner> result = partnerRepository.findByFilter("körte".toUpperCase());

        assertEquals(0, result.size());
    }
}
