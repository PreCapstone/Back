package com.springboot.apiserver.openai.controller;

import com.springboot.apiserver.openai.openaiapidto.*;
import com.springboot.apiserver.openai.exception.OpenAIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/GPT/api")
public class OpenAiApiController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final OpenAIException openAIException;
    @Autowired
    private RestTemplate restTemplate;

    public OpenAiApiController(OpenAIException openAIException) {
        this.openAIException = openAIException;
    }

    @PostMapping("/create-message")
    public String createMessage(@RequestBody RequestAdvertiseDto requestAdvertiseDto) {
        openAIException.checkRequest(requestAdvertiseDto);
        //SPRING에선 생성자 호출 방식으로 초기화하지 않고, Setter를 이용하기 때문에 기존 방식에서 변경함.
        //Controller에서 모두 처리해도 되지만, 클래스별 책임 분리를 위해 수정함.
        requestAdvertiseDto.initializeMessages();

        List<MessageDto> messages = requestAdvertiseDto.getMessages();
        CustomRequestDto customRequestDto = new CustomRequestDto(model,messages);
        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, customRequestDto, ResponseDto.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @PostMapping("/create-sd-prompt")
    public String createSdPrompt(@RequestBody RequestSDDto requestSDDto) {
        requestSDDto.initializeMessages();
        List<MessageDto> messages = requestSDDto.getMessages();
        CustomRequestDto customRequestDto = new CustomRequestDto(model,messages);
        ResponseDto chatGPTResponse = restTemplate.postForObject(apiUrl, customRequestDto, ResponseDto.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
