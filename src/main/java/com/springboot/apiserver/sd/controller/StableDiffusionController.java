package com.springboot.apiserver.sd.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.apiserver.sd.sddto.StableDiffusionRequestDto;
import com.springboot.apiserver.sd.sddto.StableDiffusionResponseDto;
import com.springboot.apiserver.sd.service.StableDiffusionService;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SD/api")
public class StableDiffusionController {

    private final StableDiffusionService stableDiffusionService;

    // 생성자 주입 방식 사용
    public StableDiffusionController(StableDiffusionService stableDiffusionService) {
        this.stableDiffusionService = stableDiffusionService;
    }

    @PostMapping("/test")
    public ResponseEntity<String> generateImage(@RequestBody StableDiffusionRequestDto requestDto) {
        try {
            Response apiResponse = stableDiffusionService.generateImage(requestDto.getPrompt(), requestDto.getInitImage());

            // 응답을 JSON으로 변환
            if (apiResponse != null && apiResponse.isSuccessful()) {
                String jsonData = apiResponse.body().string();

                // JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                StableDiffusionResponseDto responseDto = objectMapper.readValue(jsonData, StableDiffusionResponseDto.class);

                return ResponseEntity.ok(jsonData);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 생성 에러");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 생성 중 예외 발생");
        }
    }
}
