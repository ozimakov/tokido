package io.tokido.core.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;

@Component
public class MongoConnectionValidator implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public MongoConnectionValidator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            mongoTemplate.executeCommand("{ ping: 1 }");
        } catch (Exception e) {
            throw new IllegalStateException("Could not connect to MongoDB. Failing startup!", e);
        }
    }
}
