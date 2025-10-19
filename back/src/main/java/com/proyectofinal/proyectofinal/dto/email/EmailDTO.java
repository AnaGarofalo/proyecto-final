package com.proyectofinal.proyectofinal.dto.email;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailDTO {
    private String subject;
    private String to;
    private String content;
}
