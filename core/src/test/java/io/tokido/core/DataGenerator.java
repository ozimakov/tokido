package io.tokido.core;

import io.tokido.core.model.OrgDTO;
import org.instancio.Instancio;

import static org.instancio.Select.field;

public class DataGenerator {

    public static OrgDTO newOrg() {
        return Instancio.ofBlank(OrgDTO.class)
                .generate(field(OrgDTO.class, "orgName"), gen -> gen.text().word())
                .create();
    }

}
