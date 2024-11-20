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
                        " 사용자가 입력한 프롬프트를 잘 보고 Stable Diffusion에 전달되기 적절한 프롬프트를 작성해줘." +
                        "프롬프트는 영어로 작성되어야 더 잘 동작하게 된다는거 알지? " +
                        "API 호출을 통해 바로 입력될 수 있도록 다른 대답 없이 오직 Prompt만 작성해줘." +
                        "사용자가 입력한 프롬프트 : %s"
                ,userPrompt
        );
        this.messages.add(new MessageDto("user", formattedPrompt));
    }

    public void negativePrompt(String userPrompt){
                String formattedPrompt = String.format(
                        "Stable Diffusion Image to Image 모델에 사용할 negative prompts를 추천해줘. " +
                                "사용자가 입력한 프롬프트는: \"%s\". " +
                                "적합한 카테고리를 추론한 뒤, 아래 URL의 정보를 참고해 해당 카테고리에서 적합한 negative prompts를 추천해줘.\n" +
                                "URL: https://mockey.ai/blog/stable-diffusion-negative-prompt/\n" +
                                "결과는 다음 형식으로 반환해:\n" +
                                "Category: [카테고리 이름]\n" +
                                "Negative Prompts: [쉼표로 구분된 문자열]\n" +
                                "주의: 다른 부가 설명 없이 위 형식만 반환해.",
                        userPrompt
                );
        this.messages.add(new MessageDto("user", formattedPrompt));
    }

}
