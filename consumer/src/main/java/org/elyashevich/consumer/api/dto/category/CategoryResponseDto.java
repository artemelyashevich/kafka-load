package org.elyashevich.consumer.api.dto.category;

public record CategoryResponseDto(
        String id,
        String name,
        String description
) {
}
