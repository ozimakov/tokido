package io.tokido.core.repos;

import io.tokido.core.domain.App;
import io.tokido.core.domain.Org;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AppRepository extends MongoRepository<App, Long> {

    App findFirstByOrg(Org org);

}
