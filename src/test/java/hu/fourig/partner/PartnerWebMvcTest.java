package hu.fourig.partner;

import hu.fourig.partner.dto.AddressDto;
import hu.fourig.partner.dto.PartnerDto;
import hu.fourig.partner.service.PartnerService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartnerWebMvcTest extends BaseWebMvcTest {

    private static final String PARTNER_EMAIL = "elek@mail.com";
    private static final String ADDRESS_STREET = "hosszúutca";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartnerService partnerService;

    @Test
    @Order(1)
    void createPartner_ValidPartner_PartnerCreated() throws Exception {
        PartnerDto partnerDto = PartnerDto.builder().name("elek").email(PARTNER_EMAIL).build();

        mockMvc.perform(post("/api/partners").contentType(MediaType.APPLICATION_JSON).content(toJson(partnerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.name").value(partnerDto.getName()));
    }

    @Test
    @Order(2)
    void createAddress_ValidAddress_AddressCreated() throws Exception {
        Long partnerId = partnerService.listPartners(Optional.of(PARTNER_EMAIL)).get(0).getId();
        AddressDto addressDto = AddressDto.builder().city("nagyváros").street(ADDRESS_STREET).build();

        mockMvc.perform(post("/api/partners/{partnerId}/addresses", partnerId)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(addressDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(IsNull.notNullValue()))
                .andExpect(jsonPath("$.city").value(addressDto.getCity()));
    }

    @Test
    @Order(3)
    void listPartners_WithStreetFilter_PartnerWithAddress() throws Exception {
        Long partnerId = partnerService.listPartners(Optional.of(PARTNER_EMAIL)).get(0).getId();

        mockMvc.perform(get("/api/partners").param("filter", "sSzÚ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(partnerId))
                .andExpect(jsonPath("$[0].addresses[0].street").value(ADDRESS_STREET));
    }
}
