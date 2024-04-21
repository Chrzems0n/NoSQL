package com.example.demo.Repository;

import com.example.demo.Model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface StudentRepo extends MongoRepository<Student, Integer> {
    @Query("{ 'name' : ?0 }")
    Student findByName(String name);
}
