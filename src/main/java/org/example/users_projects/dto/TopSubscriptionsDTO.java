package org.example.users_projects.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TopSubscriptionsDTO {
    private String serviceName;
    private Long subscriptionCount;
}