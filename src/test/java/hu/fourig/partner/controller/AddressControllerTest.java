package hu.fourig.partner.controller;

import hu.fourig.partner.BaseWebMvcTest;
import hu.fourig.partner.dto.AddressDto;
import hu.fourig.partner.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class AddressControllerTest extends BaseWebMvcTest {

    private static final Long PARTNER_ID = 1L;

    @MockitoBean
    private AddressService addressService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listAddresses_Valid_Success() throws Exception {
        AddressDto addressDto = AddressDto.builder().id(2L).city("nagyváros").street("hosszúutca").build();
        when(addressService.listAddresses(PARTNER_ID, Optional.empty())).thenReturn(List.of(addressDto));

        mockMvc.perform(get("/api/partners/{partnerId}/addresses", PARTNER_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(addressDto.getId().toString()))
                .andExpect(jsonPath("$[0].city").value(addressDto.getCity()))
                .andExpect(jsonPath("$[0].street").value(addressDto.getStreet()));

        verify(addressService).listAddresses(PARTNER_ID, Optional.empty());
    }
}
