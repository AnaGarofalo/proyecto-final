package com.proyectofinal.proyectofinal.dto.chat_user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateChatUserDTO {
    private String email;
    private String phoneNumber;
}
