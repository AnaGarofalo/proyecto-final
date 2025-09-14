package com.proyectofinal.proyectofinal.dto.app_user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AppUserLoginDTO {
    private String email;
    private String password;
}
