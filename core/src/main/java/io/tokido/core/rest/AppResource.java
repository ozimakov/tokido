package io.tokido.core.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.tokido.core.model.AppDTO;
import io.tokido.core.service.AppService;
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
@RequestMapping(value = "/api/apps", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppResource {

    private final AppService appService;

    public AppResource(final AppService appService) {
        this.appService = appService;
    }

    @GetMapping
    public ResponseEntity<List<AppDTO>> getAllApps() {
        return ResponseEntity.ok(appService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppDTO> getApp(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(appService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createApp(@RequestBody @Valid final AppDTO appDTO) {
        final Long createdId = appService.create(appDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateApp(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AppDTO appDTO) {
        appService.update(id, appDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteApp(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = appService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        appService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
