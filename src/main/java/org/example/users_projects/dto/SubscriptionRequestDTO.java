package org.example.users_projects.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequestDTO {
    @NotBlank(message = "Service name is required")
    private String serviceName;
    @NotNull(message = "Subscription period is required")
    private SubscriptionPeriod subscriptionPeriod;
}