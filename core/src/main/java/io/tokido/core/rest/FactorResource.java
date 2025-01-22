package io.tokido.core.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.tokido.core.model.FactorDTO;
import io.tokido.core.service.FactorService;
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
@RequestMapping(value = "/api/factors", produces = MediaType.APPLICATION_JSON_VALUE)
public class FactorResource {

    private final FactorService factorService;

    public FactorResource(final FactorService factorService) {
        this.factorService = factorService;
    }

    @GetMapping
    public ResponseEntity<List<FactorDTO>> getAllFactors() {
        return ResponseEntity.ok(factorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactorDTO> getFactor(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(factorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createFactor(@RequestBody @Valid final FactorDTO factorDTO) {
        final Long createdId = factorService.create(factorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateFactor(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final FactorDTO factorDTO) {
        factorService.update(id, factorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteFactor(@PathVariable(name = "id") final Long id) {
        factorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
