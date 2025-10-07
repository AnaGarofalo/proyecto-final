package com.proyectofinal.proyectofinal.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Document extends AbstractModel {

    @Column(nullable = false)
    private String fileName;
}