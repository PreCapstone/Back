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
        System.out.println(requestAdvertiseDto.getMessages().toString());
        openAIException.checkRequest(requestAdvertiseDto);
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
