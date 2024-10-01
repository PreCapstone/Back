package com.springboot.apiserver.openaiapicontroller;

import com.springboot.apiserver.openaiapidto.RequestDto;
import com.springboot.apiserver.openaiapidto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/GPT/api")
public class OpenAiApiController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt")String prompt){
        RequestDto request = new RequestDto(model, prompt);
        ResponseDto chatGPTResponse =  restTemplate.postForObject(apiUrl, request, ResponseDto.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }


}
