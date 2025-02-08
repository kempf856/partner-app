package hu.fourig.partner.controller;

import hu.fourig.partner.dto.PartnerDto;
import hu.fourig.partner.service.PartnerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<List<PartnerDto>> listPartners(@RequestParam Optional<String> filter) {
        return ResponseEntity.ok(partnerService.listPartners(filter));
    }

    @GetMapping("/{partnerId}")
    public ResponseEntity<PartnerDto> getPartner(@PathVariable Long partnerId) {
        return ResponseEntity.ok(partnerService.getPartner(partnerId));
    }

    @PostMapping
    public ResponseEntity<PartnerDto> createPartner(@RequestBody PartnerDto partnerDto) {
        return ResponseEntity.ok(partnerService.createPartner(partnerDto));
    }

    @PutMapping("/{partnerId}")
    public ResponseEntity<PartnerDto> updatePartner(@PathVariable Long partnerId, @RequestBody PartnerDto partnerDto) {
        if (partnerId.longValue() != partnerDto.getId().longValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PartnerIds not match!");
        }
        return ResponseEntity.ok(partnerService.updatePartner(partnerDto));
    }

    @DeleteMapping("/{partnerId}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long partnerId) {
        partnerService.deletePartner(partnerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public void exportPartners(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"partners.csv\"");

        List<PartnerDto> partners = partnerService.listPartners(Optional.empty());

        try (PrintWriter writer = response.getWriter()) {
            writer.println("ID|Name|Email|Phone");
            partners.forEach(p -> writer.println(
                    String.join("|", p.getId().toString(), p.getName(), p.getEmail(), p.getPhone())));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestParam MultipartFile file) throws IOException {
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
                partnerService.createPartner(PartnerDto.builder().name(values[1]).email(values[2]).phone(values[3]).build());
            }
        }

        return ResponseEntity.ok().build();
    }
}

