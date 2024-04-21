package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class MongoDbConnectionTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testMongoDbConnection(){
        assert mongoTemplate !=null;
        assert mongoTemplate.getDb().getName().equals("NoSQL");
    }

}
