package com.example.demo.Repository;

import com.example.demo.Model.Messages;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessagesRepo extends MongoRepository<Messages, Integer> {
        List<Messages> findByStudentId(Integer studentId);
    }

