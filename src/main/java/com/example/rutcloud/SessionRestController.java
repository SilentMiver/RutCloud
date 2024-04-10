package com.example.rutcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class SessionRestController {

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        Optional<Session> sessionOptional = sessionRepository.findById(id);
        return sessionOptional.map(session -> new ResponseEntity<>(session, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        Session createdSession = sessionRepository.save(session);
        return new ResponseEntity<>(createdSession, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session) {
        if (!sessionRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        session.setId(id);
        Session updatedSession = sessionRepository.save(session);
        return new ResponseEntity<>(updatedSession, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        if (!sessionRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        sessionRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

