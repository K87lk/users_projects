package org.example.users_projects.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.users_projects.dto.SubscriptionRequestDTO;
import org.example.users_projects.dto.SubscriptionResponseDTO;
import org.example.users_projects.dto.TopSubscriptionsDTO;
import org.example.users_projects.mapper.SubscriptionMapper;
import org.example.users_projects.model.Subscription;
import org.example.users_projects.model.User;
import org.example.users_projects.repository.SubscriptionRepository;
import org.example.users_projects.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionMapper subscriptionMapper;

    public List<SubscriptionResponseDTO> getUserSubscriptions(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        log.info("Getting subscriptions for user with id: {}", userId);
        return subscriptionRepository.findByUserId(userId).stream()
                .map(subscriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubscriptionResponseDTO addSubscription(UUID userId, SubscriptionRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        log.info("Adding subscription for user with id: {}", userId);

        Subscription subscription = new Subscription();
        subscription.setServiceName(subscriptionMapper.stringToSubscriptionType(request.getServiceName()));
        subscription.setSubscriptionPeriod(request.getSubscriptionPeriod());
        subscription.setUser(user);

        Subscription saved = subscriptionRepository.save(subscription);

        log.info("Subscription added successfully");
        return subscriptionMapper.toDTO(saved);
    }

    @Transactional
    public void deleteSubscription(UUID userId, UUID subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Subscription not found with id: " + subscriptionId));

        if (!subscription.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Subscription does not belong to user with id: " + userId);
        }
        log.info("Deleting subscription with id: {}", subscriptionId);
        subscriptionRepository.delete(subscription);
    }

    public List<TopSubscriptionsDTO> getTopSubscriptions() {
        log.info("Getting top 3 subscriptions");
        return subscriptionRepository.findTop3Subscriptions()
                .stream()
                .map(p -> new TopSubscriptionsDTO(
                        p.getServiceName(),
                        p.getSubscriptionCount()))
                .collect(Collectors.toList());
    }
}