package hu.fourig.partner.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;
    private String city;
    private String street;
}
