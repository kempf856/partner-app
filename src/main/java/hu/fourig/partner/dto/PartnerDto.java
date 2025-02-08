package hu.fourig.partner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<AddressDto> addresses;
}
