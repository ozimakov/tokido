package io.tokido.core.repos;

import io.tokido.core.domain.Org;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface OrgRepository extends MongoRepository<Org, Long> {
}
