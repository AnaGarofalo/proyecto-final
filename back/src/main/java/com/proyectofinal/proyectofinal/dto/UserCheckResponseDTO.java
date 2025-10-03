package com.proyectofinal.proyectofinal.dto;

import com.proyectofinal.proyectofinal.model.ChatUser;
import com.proyectofinal.proyectofinal.types.PremadeResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserCheckResponseDTO {
    private ChatUser chatUser;
    private PremadeResponse premadeResponse;

    public UserCheckResponseDTO(PremadeResponse premadeResponse) {
        this.premadeResponse = premadeResponse;
    }

    public UserCheckResponseDTO(ChatUser chatUser) {
        this.chatUser = chatUser;
    }
}
