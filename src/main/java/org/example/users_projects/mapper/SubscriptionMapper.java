package org.example.users_projects.mapper;

import org.example.users_projects.dto.SubscriptionRequestDTO;
import org.example.users_projects.dto.SubscriptionResponseDTO;
import org.example.users_projects.model.Subscription;
import org.example.users_projects.dto.SubscriptionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "serviceName", source = "serviceName", qualifiedByName = "stringToSubscriptionType")
    Subscription toEntity(SubscriptionRequestDTO subscriptionRequestDTO);

    @Mapping(target = "endTime", source = "endDate")
    @Mapping(target = "startTime", source = "startDate")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "serviceName", source = "serviceName", qualifiedByName = "subscriptionTypeToString")
    @Mapping(target = "displayPeriod", source = "subscriptionPeriod.displayName")
    SubscriptionResponseDTO toDTO(Subscription subscription);

    @Named("stringToSubscriptionType")
    default SubscriptionType stringToSubscriptionType(String serviceName) {
        return SubscriptionType.valueOf(serviceName);
    }

    @Named("subscriptionTypeToString")
    default String subscriptionTypeToString(SubscriptionType serviceName) {
        return serviceName.name();
    }
}
