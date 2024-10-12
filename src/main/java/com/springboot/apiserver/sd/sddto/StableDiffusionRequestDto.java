package com.springboot.apiserver.sd.sddto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StableDiffusionRequestDto {
    private String prompt;
    private String initImage;
}
