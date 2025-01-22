package io.tokido.core.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.tokido.core.model.OrgDTO;
import io.tokido.core.service.OrgService;
import io.tokido.core.util.ReferencedException;
import io.tokido.core.util.ReferencedWarning;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/orgs", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrgResource {

    private final OrgService orgService;

    public OrgResource(final OrgService orgService) {
        this.orgService = orgService;
    }

    @GetMapping
    public ResponseEntity<List<OrgDTO>> getAllOrgs() {
        return ResponseEntity.ok(orgService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrgDTO> getOrg(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(orgService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createOrg(@RequestBody @Valid final OrgDTO orgDTO) {
        final Long createdId = orgService.create(orgDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateOrg(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final OrgDTO orgDTO) {
        orgService.update(id, orgDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrg(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = orgService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        orgService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
