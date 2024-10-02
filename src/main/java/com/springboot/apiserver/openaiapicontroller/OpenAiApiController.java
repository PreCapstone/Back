package com.springboot.apiserver.openaiapicontroller;

import com.springboot.apiserver.openaiapidto.CustomRequstDto;
import com.springboot.apiserver.openaiapidto.MessageDto;
import com.springboot.apiserver.openaiapidto.RequestDto;
import com.springboot.apiserver.openaiapidto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/GPT/api")
public class OpenAiApiController {
//    테스트용 가장 싼 모델
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

//    테스트를 위한 부분 주석처리
//    @GetMapping("/chat")
//    public String chat(@RequestParam(name = "prompt") String prompt) {
//        RequestDto request = new RequestDto(model, prompt);
//        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, request, ResponseDto.class);
//        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
//    }
//
//    @PostMapping("/chat-post")
//    public String postChat(@RequestBody RequestDto request) {
//        request.setModel(model);
//        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, request, ResponseDto.class);
//        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
//    }

    @PostMapping("/chat-test")
    public String testGenerateAd(@RequestBody RequestDto requestDto) {
        String formattedPrompt = String.format(
                "%s 톤으로 광고 문구를 만들어줘. 광고를 듣게 될 사람은 %s야. 광고 카피에 포함되어야 할 메인 키워드는 : %s. %s",
                requestDto.getMood(),
                requestDto.getTarget(),
                requestDto.getKeyword(),
                requestDto.getPrompt()
        );

        // 새로운 OpenAiRequestDto 생성
        List<MessageDto> messages = new ArrayList<>();
        messages.add(new MessageDto("user", formattedPrompt));  // 프롬프트 메시지 추가 - 포맹틴된걸로. 수정 해야함
        CustomRequstDto customRequstDto = new CustomRequstDto(model,messages);
        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, customRequstDto, ResponseDto.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();

    }
}
