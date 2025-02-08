package hu.fourig.partner.controller;

import hu.fourig.partner.dto.AddressDto;
import hu.fourig.partner.service.AddressService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partners/{partnerId}/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> listAddresses(@PathVariable Long partnerId, @RequestParam Optional<String> filter) {
        return ResponseEntity.ok(addressService.listAddresses(partnerId, filter));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long partnerId, @PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.getAddress(partnerId, addressId));
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@PathVariable Long partnerId, @RequestBody AddressDto addressDto) {
        return ResponseEntity.ok(addressService.createAddress(partnerId, addressDto));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long partnerId, @PathVariable Long addressId, @RequestBody AddressDto addressDto) {
        if (addressId.longValue() != addressDto.getId().longValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "AddressIds not match!");
        }
        return ResponseEntity.ok(addressService.updateAddress(partnerId, addressDto));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long partnerId, @PathVariable Long addressId) {
        addressService.deleteAddress(partnerId, addressId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public void exportAddresses(@PathVariable Long partnerId, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"addresses_for_partner_" + partnerId + ".csv\"");

        List<AddressDto> addresses = addressService.listAddresses(partnerId, Optional.empty());

        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID|City|Street");
            addresses.forEach(a -> writer.println(
                    String.join("|", a.getId().toString(), a.getCity(), a.getStreet())));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@PathVariable Long partnerId, @RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty!");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("ISO-8859-2")))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }

                String[] values = line.split("\\|");
                addressService.createAddress(partnerId, AddressDto.builder().city(values[1]).street(values[2]).build());
            }
        }

        return ResponseEntity.ok().build();
    }
}

