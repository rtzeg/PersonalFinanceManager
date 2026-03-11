package com.pfm.notification.service;

import com.pfm.common.exception.ApiException;
import com.pfm.notification.entity.Notification;
import com.pfm.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    public List<Notification> findAll(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Notification createNotification(Long userId, String title, String message, String type) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        return repository.save(notification);
    }

    public Notification markRead(Long userId, Long id) {
        Notification notification = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException("Notification not found"));
        notification.setRead(true);
        return repository.save(notification);
    }

    public void markReadAll(Long userId) {
        List<Notification> list = findAll(userId);
        list.forEach(n -> n.setRead(true));
        repository.saveAll(list);
    }
}
