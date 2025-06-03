package org.example.users_projects.repository;

import org.example.users_projects.dto.TopSubscriptionsDTO;
import org.example.users_projects.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByUserId(UUID userId);

    @Query(value = "SELECT s.service_name as serviceName, COUNT(s) as subscriptionCount " +
           "FROM subscriptions s GROUP BY s.service_name ORDER BY COUNT(s) DESC LIMIT 3", nativeQuery = true)
    List<TopSubscriptionsProjection> findTop3Subscriptions();

    interface TopSubscriptionsProjection {
        String getServiceName();
        Long getSubscriptionCount();
    }
}