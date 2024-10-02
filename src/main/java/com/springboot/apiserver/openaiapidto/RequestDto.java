package com.springboot.apiserver.openaiapidto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RequestDto {
    private List<MessageDto> messages; //required
    private String model; //required

//    여기 아래부터는 open ai 서버에 전달되면 500 Err
    private String mood; //사용자 설정 분위기 --> 설정용 API 추가 필요, nullable?
    private String target; //사용자 설정 타겟 층 연령대 --> 설정용 API 추가 필요, nullable
    private String keyword; //사용자 설정 메인 키워드 --> 설정용 API 추가 필요, nullable?
    private String prompt;//사용자 설정 문장 --> nullable?
    // OpenAI API에 전달할 프롬프트 생성자
    public RequestDto( String mood, String target, String keyword, String prompt) {
//        this.model = model;
        this.messages = new ArrayList<MessageDto>(); // 메시지 리스트 초기화
        // 프롬프트 생성: 사용자 설정을 반영하여 메시지 내용 구성
        String formattedPrompt = String.format(
                "Create an ad copy with a %s tone. Target audience is %s. Main keyword: %s. %s",
                mood != null ? mood : "neutral",
                target != null ? target : "general audience",
                keyword != null ? keyword : "product",
                prompt != null ? prompt : "Write a creative ad."
        );
        System.out.println("========================================");
        System.out.println(formattedPrompt);
        System.out.println("========================================");
        // 생성된 프롬프트를 메시지로 추가
        this.messages.add(new MessageDto("user", formattedPrompt));
    }


    public RequestDto(String model, String prompt) {
        this.model=model;
        this.messages = new ArrayList<>();
    }
}
