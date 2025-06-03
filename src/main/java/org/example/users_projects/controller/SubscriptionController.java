package org.example.users_projects.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.users_projects.dto.SubscriptionRequestDTO;
import org.example.users_projects.dto.SubscriptionResponseDTO;
import org.example.users_projects.dto.TopSubscriptionsDTO;
import org.example.users_projects.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/users/{id}/subscriptions")
    public ResponseEntity<List<SubscriptionResponseDTO>> getUserSubscriptions(@PathVariable UUID id) {
        return ResponseEntity.ok(subscriptionService.getUserSubscriptions(id));
    }

    @PostMapping("/users/{id}/subscriptions")
    public ResponseEntity<SubscriptionResponseDTO> addSubscription(
            @PathVariable UUID id,
            @Valid @RequestBody SubscriptionRequestDTO subscriptionRequestDTO) {
        return new ResponseEntity<>(
                subscriptionService.addSubscription(id, subscriptionRequestDTO),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}/subscriptions/{sub_id}")
    public ResponseEntity<Void> deleteSubscription(
            @PathVariable("id") UUID userId,
            @PathVariable("sub_id") UUID subscriptionId) {
        subscriptionService.deleteSubscription(userId, subscriptionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subscriptions/top")
    public ResponseEntity<List<TopSubscriptionsDTO>> getTopSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getTopSubscriptions());
    }
}