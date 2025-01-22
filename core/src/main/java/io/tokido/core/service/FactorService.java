package io.tokido.core.service;

import io.tokido.core.domain.App;
import io.tokido.core.domain.Factor;
import io.tokido.core.model.FactorDTO;
import io.tokido.core.repos.AppRepository;
import io.tokido.core.repos.FactorRepository;
import io.tokido.core.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FactorService {

    private final FactorRepository factorRepository;
    private final AppRepository appRepository;

    public FactorService(final FactorRepository factorRepository,
            final AppRepository appRepository) {
        this.factorRepository = factorRepository;
        this.appRepository = appRepository;
    }

    public List<FactorDTO> findAll() {
        final List<Factor> factors = factorRepository.findAll(Sort.by("id"));
        return factors.stream()
                .map(factor -> mapToDTO(factor, new FactorDTO()))
                .toList();
    }

    public FactorDTO get(final Long id) {
        return factorRepository.findById(id)
                .map(factor -> mapToDTO(factor, new FactorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final FactorDTO factorDTO) {
        final Factor factor = new Factor();
        mapToEntity(factorDTO, factor);
        return factorRepository.save(factor).getId();
    }

    public void update(final Long id, final FactorDTO factorDTO) {
        final Factor factor = factorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(factorDTO, factor);
        factorRepository.save(factor);
    }

    public void delete(final Long id) {
        factorRepository.deleteById(id);
    }

    private FactorDTO mapToDTO(final Factor factor, final FactorDTO factorDTO) {
        factorDTO.setId(factor.getId());
        factorDTO.setFactorName(factor.getFactorName());
        factorDTO.setSecret(factor.getSecret());
        factorDTO.setActive(factor.getActive());
        factorDTO.setApp(factor.getApp() == null ? null : factor.getApp().getId());
        return factorDTO;
    }

    private Factor mapToEntity(final FactorDTO factorDTO, final Factor factor) {
        factor.setFactorName(factorDTO.getFactorName());
        factor.setSecret(factorDTO.getSecret());
        factor.setActive(factorDTO.getActive());
        final App app = factorDTO.getApp() == null ? null : appRepository.findById(factorDTO.getApp())
                .orElseThrow(() -> new NotFoundException("app not found"));
        factor.setApp(app);
        return factor;
    }

}
