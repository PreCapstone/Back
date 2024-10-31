package com.springboot.apiserver.sd.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.apiserver.sd.sddto.StableDiffusionRequestDto;
import com.springboot.apiserver.sd.sddto.StableDiffusionResponseDto;
import com.springboot.apiserver.sd.service.StableDiffusionService;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/SD/api")
public class StableDiffusionController {

    private final StableDiffusionService stableDiffusionService;
    private final ObjectMapper objectMapper;

    // 생성자 주입 방식 사용
    public StableDiffusionController(StableDiffusionService stableDiffusionService, ObjectMapper objectMapper) {
        this.stableDiffusionService = stableDiffusionService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/create-image")
    public ResponseEntity<StableDiffusionResponseDto> generateImage(@RequestBody StableDiffusionRequestDto requestDto) {
        System.out.println(requestDto.getPrompt());
        try {
            Response response = stableDiffusionService.generateImage(requestDto.getPrompt(), requestDto.getInitImage());
            System.out.println(response);
            if (response != null && response.isSuccessful()) {
                String jsonData = response.body().string();
                JsonNode rootNode = objectMapper.readTree(jsonData);
                String imageUrl = rootNode.path("output").get(0).asText();
                double time = rootNode.path("generationTime").asDouble();
                String base64EncodedUrl = Base64.getUrlEncoder().encodeToString(imageUrl.getBytes());
                StableDiffusionResponseDto stableDiffusionResponseDto=
                        new StableDiffusionResponseDto(imageUrl,base64EncodedUrl,time);
                return ResponseEntity.ok(stableDiffusionResponseDto);
            }
            else {
                return ResponseEntity.status(500).body(new StableDiffusionResponseDto(null,"image error",-1.0));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new StableDiffusionResponseDto(
                    null, "예외 발생: "+e,-1.0));
        }
    }
}
