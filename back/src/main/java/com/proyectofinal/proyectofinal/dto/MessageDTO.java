package com.proyectofinal.proyectofinal.dto;

import com.proyectofinal.proyectofinal.types.MessageOrigin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String content;
    private MessageOrigin origin;
}
