package hu.fourig.partner.service;

import hu.fourig.partner.dto.PartnerDto;
import hu.fourig.partner.mapper.PartnerMapper;
import hu.fourig.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerMapper partnerMapper;
    private final PartnerRepository partnerRepository;

    @Transactional
    public List<PartnerDto> listPartners(Optional<String> filter) {
        return filter.map(String::toUpperCase).map(partnerRepository::findByFilter)
                .orElseGet(partnerRepository::findAll)
                .stream().map(partnerMapper::map).toList();
    }

    public PartnerDto getPartner(Long partnerId) {
        return partnerMapper.map(partnerRepository.findById(partnerId).orElseThrow());
    }

    public PartnerDto createPartner(PartnerDto partnerDto) {
        partnerDto.setId(null);
        return updatePartner(partnerDto);
    }

    public PartnerDto updatePartner(PartnerDto partnerDto) {
        return partnerMapper.map(partnerRepository.save(partnerMapper.map(partnerDto)));
    }

    public void deletePartner(Long partnerId) {
        partnerRepository.deleteById(partnerId);
    }
}
