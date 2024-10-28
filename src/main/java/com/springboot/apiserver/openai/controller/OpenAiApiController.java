package com.springboot.apiserver.openai.controller;

import com.springboot.apiserver.openai.openaiapidto.CustomRequestDto;
import com.springboot.apiserver.openai.openaiapidto.MessageDto;
import com.springboot.apiserver.openai.openaiapidto.RequestDto;
import com.springboot.apiserver.openai.openaiapidto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @PostMapping("/create-message")
    public String createMessage(@RequestBody RequestDto requestDto) {
        //SPRING에선 생성자 호출 방식으로 초기화하지 않고, Setter를 이용하기 때문에 기존 방식에서 변경함.
        //Controller에서 모두 처리해도 되지만, 클래스별 책임 분리를 위해 수정함.
        requestDto.initializeMessages();

        List<MessageDto> messages = requestDto.getMessages();
        CustomRequestDto customRequestDto = new CustomRequestDto(model,messages);
        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, customRequestDto, ResponseDto.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();

    }
}
