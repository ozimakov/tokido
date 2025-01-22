package io.tokido.core.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
@Getter
@Setter
public class App {

    @Id
    private Long id;

    @NotNull
    @Size(max = 255)
    private String appName;

    @DocumentReference(lazy = true)
    @NotNull
    private Org org;

    @DocumentReference(lazy = true, lookup = "{ 'app' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<Factor> factors;

    @CreatedDate
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    private OffsetDateTime lastUpdated;

    @Version
    private Integer version;

}
