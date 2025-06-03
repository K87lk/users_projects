package org.example.users_projects.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SubscriptionPeriod {
    ONE_MONTH(30, "1 месяц"),
    THREE_MONTHS(90, "3 месяца"),
    SIX_MONTHS(180, "6 месяцев"),
    ONE_YEAR(365, "1 год");

    private final int days;
    private final String displayName;

    public static SubscriptionPeriod fromDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(sp -> sp.displayName.equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid period: " + displayName));
    }
}
