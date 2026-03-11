package com.pfm.notification.controller;

import com.pfm.common.util.CurrentUserService;
import com.pfm.notification.entity.Notification;
import com.pfm.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService service;
    private final CurrentUserService current;
    @GetMapping public List<Notification> all(){return service.findAll(current.getUserId());}
    @PatchMapping("/{id}/read") public Notification markRead(@PathVariable Long id){return service.markRead(current.getUserId(), id);}    
    @PatchMapping("/read-all") public void markReadAll(){service.markReadAll(current.getUserId());}
}
