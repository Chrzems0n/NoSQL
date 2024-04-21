package com.example.demo.Controller;

//mongodb://localhost:27017
import com.example.demo.Model.Messages;
import com.example.demo.Model.Student;
import com.example.demo.Repository.MessagesRepo;
import com.example.demo.Repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {
    @Autowired
    StudentRepo studentRepo;

    @Autowired
    MessagesRepo messagesRepo;


    //DOdanie studenta
    @PostMapping("/addStudent")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        if (studentRepo.existsById(student.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Student with ID " + student.getId() + " already exists.");
        } else {
            Student savedStudent = studentRepo.save(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        }
    }
    //Wyświetlenie studenta po id
    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        Student student = studentRepo.findById(id).orElse(null);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Usuwanie studenta po ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Integer id) {
        if (studentRepo.existsById(id)) {
            studentRepo.deleteById(id);
            return ResponseEntity.ok("Student with ID " + id + " has been successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student with ID " + id + " not found.");
        }
    }

    //Uzyskanie lsity wszystkich studentów
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }


    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteStudentByName(@PathVariable String name) {
        Student student = studentRepo.findByName(name);
        if (student != null) {
            studentRepo.delete(student);
            return ResponseEntity.ok("Student with name " + name + " has been successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student with name " + name + " not found.");
        }
    }

    //zwróecenie wiadomosci studenta
    @GetMapping("/messages")
    public List<Messages> getStudentMessages(@RequestParam("id") Integer id) {
        return messagesRepo.findByStudentId(id);
    }

    @GetMapping("/messages/byName")
    public ResponseEntity<?> getStudentMessagesByName(@RequestParam("name") String name) {
        Student student = studentRepo.findByName(name);
        if (student != null) {
            List<Messages> messages = messagesRepo.findByStudentId(student.getId());
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Dodawanie nowej wiadomości
    @PostMapping("/messages/addMessage/{studentId}")
    public ResponseEntity<Messages> addMessage(@PathVariable Integer studentId, @RequestBody Messages message) {
        // Sprawdzamy, czy istnieje student o podanym ID
        if (!studentExists(studentId)) {
            throw new StudentNotFoundException("Nie ma studenta o takim ID");
        }

        // Ustawiamy ID studenta dla wiadomości
        message.setStudentId(studentId);

        // Zapisujemy wiadomość
        Messages savedMessage = messagesRepo.save(message);

        // Zwracamy odpowiedź z kodem HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }


    @GetMapping("/messages/{messageId}/student")
    public ResponseEntity<Student> getStudentByMessageId(@PathVariable String messageId) {
        // Pobierz wiadomość na podstawie jej identyfikatora
        Optional<Messages> messageOptional = messagesRepo.findById(Integer.valueOf(messageId));

        // Sprawdź, czy wiadomość istnieje
        if (messageOptional.isPresent()) {
            // Pobierz identyfikator studenta z wiadomości
            Integer studentId = messageOptional.get().getStudentId();

            // Użyj identyfikatora studenta, aby znaleźć odpowiedniego studenta w bazie danych
            Optional<Student> studentOptional = studentRepo.findById(Integer.valueOf(studentId.toString()));

            // Sprawdź, czy student istnieje
            if (studentOptional.isPresent()) {
                // Jeśli student istnieje, zwróć go jako odpowiedź
                return ResponseEntity.ok(studentOptional.get());
            } else {
                // Jeśli student nie istnieje, zwróć odpowiedź z kodem 404 Not Found
                return ResponseEntity.notFound().build();
            }
        } else {
            // Jeśli wiadomość nie istnieje, zwróć odpowiedź z kodem 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
//pomocnicze

    private boolean studentExists(Integer studentId) {
        // Szukamy studenta o danym ID w bazie danych
        Optional<Student> studentOptional = studentRepo.findById(studentId);

        // Zwracamy true, jeśli student o danym ID istnieje, w przeciwnym razie false
        return studentOptional.isPresent();
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static
    class StudentNotFoundException extends RuntimeException {
        public StudentNotFoundException(String message) {
            super(message);
        }
    }

}