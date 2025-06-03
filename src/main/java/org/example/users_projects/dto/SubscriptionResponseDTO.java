package org.example.users_projects.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SubscriptionResponseDTO {
    private String serviceName;
    private UUID userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String displayPeriod;
}
