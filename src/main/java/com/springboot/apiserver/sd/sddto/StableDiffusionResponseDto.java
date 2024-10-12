package com.springboot.apiserver.sd.sddto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//status, tip 등 이상한 필드가 너무 많아서 추가한 어노테이션
//@JsonIgnoerProperties 추가시 명시된 필드 외의 것은 무시함
//없으면 에러 ㅠ_ㅠ
@JsonIgnoreProperties(ignoreUnknown = true)
public class StableDiffusionResponseDto {
    private boolean successful;
    private boolean redirect;
    private String status;
}
