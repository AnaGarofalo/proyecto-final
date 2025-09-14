package com.proyectofinal.proyectofinal.mapper.AppUser;

import com.proyectofinal.proyectofinal.dto.app_user.AppUserMinimalDTO;
import com.proyectofinal.proyectofinal.model.AppUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppUserMinimalDTOMapper {
    public static AppUserMinimalDTO fromEntity(AppUser model) {
        return AppUserMinimalDTO.builder()
                .externalId(model.getExternalId())
                .email(model.getEmail())
                .blockedAt(model.getBlockedAt())
                .build();
    }
}
