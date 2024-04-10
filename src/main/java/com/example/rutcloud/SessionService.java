package com.example.rutcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    // Метод для создания новой сессии
    @Transactional
    public void createSession(Session session) {
        sessionRepository.save(session);
    }
    @Transactional
    public  void  deleteSession(Session session){
        sessionRepository.delete(session);
    }
}

