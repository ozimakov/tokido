package io.tokido.core.repos;

import io.tokido.core.domain.App;
import io.tokido.core.domain.Factor;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface FactorRepository extends MongoRepository<Factor, Long> {

    Factor findFirstByApp(App app);

}
