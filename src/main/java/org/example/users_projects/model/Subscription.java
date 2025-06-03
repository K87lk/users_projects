package org.example.users_projects.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.users_projects.dto.SubscriptionPeriod;
import org.example.users_projects.dto.SubscriptionType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, name = "service_name")
    @Enumerated(EnumType.STRING)
    private SubscriptionType serviceName;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime startDate;

    @UpdateTimestamp
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "subscription_period")
    private SubscriptionPeriod subscriptionPeriod;

    @PrePersist
    @PreUpdate
    protected void calculateDates() {
        if (this.startDate == null) {
            this.startDate = LocalDateTime.now();
        }
        if (this.subscriptionPeriod != null && this.startDate != null) {
            this.endDate = startDate.plusDays(subscriptionPeriod.getDays());
        }
    }
}