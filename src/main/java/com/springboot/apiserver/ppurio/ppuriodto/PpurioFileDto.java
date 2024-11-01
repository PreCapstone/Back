package com.springboot.apiserver.ppurio.ppuriodto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PpurioFileDto {
    private String name;
    private int size;
    private String Data;
}
