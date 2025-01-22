package io.tokido.core.repos;

import io.tokido.core.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, Long> {
}
