package com.springboot.apiserver.openai.openaiapidto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class RequestSDDto {
    private List<MessageDto> messages = new ArrayList<>(); //required
    private String model; //required
    private String userPrompt;
    public void initializeMessages() {
        String formattedPrompt = String.format(
                "Stable Diffusion의 Image To Image 모델에 입력할 프롬프트가 필요해." +
                        " 사용자가 입력한 프롬프트g를 잘 보고 Stable Diffusion에 전달되기 적절한 프롬프트를 작성해줘." +
                        "프롬프트는 영어로 작성되어야 더 잘 동작하게 된다는거 알지? " +
                        "API 호출을 통해 바로 입력될 수 있도록 다른 대답 없이 오직 Prompt만 작성해줘." +
                        "사용자가 입력한 프롬프트 : %s"
                ,userPrompt
        );
        this.messages.add(new MessageDto("user", formattedPrompt));

    }

}
