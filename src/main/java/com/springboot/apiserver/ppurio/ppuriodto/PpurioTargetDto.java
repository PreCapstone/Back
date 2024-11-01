package com.springboot.apiserver.ppurio.ppuriodto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PpurioTargetDto {
    private String to;
    private Map<String, String> changeWord;
    private String name;
}
