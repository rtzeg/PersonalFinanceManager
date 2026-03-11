package com.pfm.notification.service;

import com.pfm.notification.entity.Notification;
import com.pfm.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;
    public List<Notification> findAll(Long userId){return repository.findByUserIdOrderByCreatedAtDesc(userId);}    
    public Notification createNotification(Long userId, String title, String message, String type){
        Notification n=new Notification();n.setUserId(userId);n.setTitle(title);n.setMessage(message);n.setType(type);return repository.save(n);
    }
    public Notification markRead(Long id){Notification n=repository.findById(id).orElseThrow();n.setRead(true);return repository.save(n);}    
    public void markReadAll(Long userId){var list=findAll(userId);list.forEach(n->n.setRead(true));repository.saveAll(list);}    
}
