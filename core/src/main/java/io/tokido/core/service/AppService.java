package io.tokido.core.service;

import io.tokido.core.domain.App;
import io.tokido.core.domain.Factor;
import io.tokido.core.domain.Org;
import io.tokido.core.model.AppDTO;
import io.tokido.core.repos.AppRepository;
import io.tokido.core.repos.FactorRepository;
import io.tokido.core.repos.OrgRepository;
import io.tokido.core.util.NotFoundException;
import io.tokido.core.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AppService {

    private final AppRepository appRepository;
    private final OrgRepository orgRepository;
    private final FactorRepository factorRepository;

    public AppService(final AppRepository appRepository, final OrgRepository orgRepository,
            final FactorRepository factorRepository) {
        this.appRepository = appRepository;
        this.orgRepository = orgRepository;
        this.factorRepository = factorRepository;
    }

    public List<AppDTO> findAll() {
        final List<App> apps = appRepository.findAll(Sort.by("id"));
        return apps.stream()
                .map(app -> mapToDTO(app, new AppDTO()))
                .toList();
    }

    public AppDTO get(final Long id) {
        return appRepository.findById(id)
                .map(app -> mapToDTO(app, new AppDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AppDTO appDTO) {
        final App app = new App();
        mapToEntity(appDTO, app);
        return appRepository.save(app).getId();
    }

    public void update(final Long id, final AppDTO appDTO) {
        final App app = appRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(appDTO, app);
        appRepository.save(app);
    }

    public void delete(final Long id) {
        appRepository.deleteById(id);
    }

    private AppDTO mapToDTO(final App app, final AppDTO appDTO) {
        appDTO.setId(app.getId());
        appDTO.setAppName(app.getAppName());
        appDTO.setOrg(app.getOrg() == null ? null : app.getOrg().getId());
        return appDTO;
    }

    private App mapToEntity(final AppDTO appDTO, final App app) {
        app.setAppName(appDTO.getAppName());
        final Org org = appDTO.getOrg() == null ? null : orgRepository.findById(appDTO.getOrg())
                .orElseThrow(() -> new NotFoundException("org not found"));
        app.setOrg(org);
        return app;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final App app = appRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Factor appFactor = factorRepository.findFirstByApp(app);
        if (appFactor != null) {
            referencedWarning.setKey("app.factor.app.referenced");
            referencedWarning.addParam(appFactor.getId());
            return referencedWarning;
        }
        return null;
    }

}
