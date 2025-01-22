package io.tokido.core.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FactorDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String factorName;

    @Size(max = 255)
    private String secret;

    @NotNull
    private Boolean active;

    @NotNull
    private Long app;

}
