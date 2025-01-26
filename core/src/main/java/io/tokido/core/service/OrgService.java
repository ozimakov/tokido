package io.tokido.core.service;

import io.tokido.core.domain.App;
import io.tokido.core.domain.Org;
import io.tokido.core.domain.User;
import io.tokido.core.model.OrgDTO;
import io.tokido.core.repos.AppRepository;
import io.tokido.core.repos.OrgRepository;
import io.tokido.core.repos.UserRepository;
import io.tokido.core.util.NotFoundException;
import io.tokido.core.util.ReferencedWarning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrgService {

    private final OrgRepository orgRepository;
    private final UserRepository userRepository;
    private final AppRepository appRepository;

    public OrgService(final OrgRepository orgRepository, final UserRepository userRepository,
            final AppRepository appRepository) {
        this.orgRepository = orgRepository;
        this.userRepository = userRepository;
        this.appRepository = appRepository;
    }

    public List<OrgDTO> findAll() {
        final List<Org> orgs = orgRepository.findAll(Sort.by("id"));
        return orgs.stream()
                .map(org -> mapToDTO(org, new OrgDTO()))
                .toList();
    }

    public OrgDTO get(final Long id) {
        return orgRepository.findById(id)
                .map(org -> mapToDTO(org, new OrgDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OrgDTO orgDTO) {
        final Org org = new Org();
        mapToEntity(orgDTO, org);
        return orgRepository.save(org).getId();
    }

    public void update(final Long id, final OrgDTO orgDTO) {
        final Org org = orgRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orgDTO, org);
        orgRepository.save(org);
    }

    public void delete(final Long id) {
        orgRepository.deleteById(id);
    }

    private OrgDTO mapToDTO(final Org org, final OrgDTO orgDTO) {
        orgDTO.setId(org.getId());
        orgDTO.setOrgName(org.getOrgName());
        orgDTO.setOwners(org.getOwners().stream()
                .map(user -> user.getId())
                .toList());
        return orgDTO;
    }

    private Org mapToEntity(final OrgDTO orgDTO, final Org org) {
        org.setOrgName(orgDTO.getOrgName());
        final List<User> owners = iterableToList(userRepository.findAllById(
                orgDTO.getOwners() == null ? Collections.emptyList() : orgDTO.getOwners()));
        if (owners.size() != (orgDTO.getOwners() == null ? 0 : orgDTO.getOwners().size())) {
            throw new NotFoundException("one of owners not found");
        }
        org.setOwners(new ArrayList<>(owners));
        return org;
    }

    private <T> List<T> iterableToList(final Iterable<T> iterable) {
        final List<T> list = new ArrayList<T>();
        iterable.forEach(item -> list.add(item));
        return list;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Org org = orgRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final App orgApp = appRepository.findFirstByOrg(org);
        if (orgApp != null) {
            referencedWarning.setKey("org.app.org.referenced");
            referencedWarning.addParam(orgApp.getId());
            return referencedWarning;
        }
        return null;
    }

}
