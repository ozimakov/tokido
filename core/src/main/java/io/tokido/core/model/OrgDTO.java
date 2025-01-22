package io.tokido.core.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrgDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String orgName;

    private List<Long> owners;

}
