package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Messages {

    @Id
    private String id;
    private Integer studentId;
    private String content;

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
}
