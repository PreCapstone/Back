package com.springboot.apiserver.sd.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.apiserver.s3.uploader.S3Uploader;
import com.springboot.apiserver.sd.sddto.StableDiffusionRequestDto;
import com.springboot.apiserver.sd.sddto.StableDiffusionResponseDto;
import com.springboot.apiserver.sd.service.StableDiffusionService;
import com.springboot.apiserver.user.images.entity.UserImages;
import com.springboot.apiserver.user.images.entity.UserImagesRepository;
import com.springboot.apiserver.util.LoggingUtil;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/SD/api")
public class StableDiffusionController {

    private final StableDiffusionService stableDiffusionService;
    private final ObjectMapper objectMapper;
    private final S3Uploader s3Uploader;
    private final UserImagesRepository userImagesRepository;
    // 생성자 주입 방식 사용
    public StableDiffusionController(StableDiffusionService stableDiffusionService, ObjectMapper objectMapper,
                                     S3Uploader s3Uploader, UserImagesRepository userImagesRepository) {
        this.stableDiffusionService = stableDiffusionService;
        this.objectMapper = objectMapper;
        this.s3Uploader = s3Uploader;
        this.userImagesRepository = userImagesRepository;
    }

    @PostMapping("/create-image")
    public ResponseEntity<StableDiffusionResponseDto> generateImage(@RequestBody StableDiffusionRequestDto requestDto) {
        int userId = requestDto.getId();
        if(userId==0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StableDiffusionResponseDto(null, "idError", -1.0,null));
        }
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = userId + "_" + timestamp + ".png";
        try {
            Response response = stableDiffusionService.generateImage(requestDto.getPrompt(), requestDto.getInitImage(),requestDto.isNegativePrompt());
            LoggingUtil.logGPTResponse(String.valueOf(response));
            if (response != null && response.isSuccessful()) {
                String jsonData = response.body().string();
                JsonNode rootNode = objectMapper.readTree(jsonData);
                System.out.println(jsonData);
                LoggingUtil.logSDResponse(jsonData);
                String imageUrl = rootNode.path("output").get(0).asText();
                double time = rootNode.path("generationTime").asDouble();
                Path filePath = Path.of(fileName);
                try (InputStream inputStream = new URL(imageUrl).openStream()) {
                    Files.copy(inputStream, filePath);
                }
                String uploadedImageUrl = s3Uploader.upload(filePath.toString());
                Files.deleteIfExists(filePath);

                UserImages userImages = new UserImages();
                userImages.setUserId(userId);
                userImages.setUserImage(uploadedImageUrl);
                userImages.setSampleImage(requestDto.getInitImage());
                userImages.setPrompt(requestDto.getPrompt());
                userImages.setCreatedAt(LocalDateTime.now());
                userImages.setTakeTime(time);
                userImagesRepository.save(userImages);

                String base64EncodedUrl = Base64.getUrlEncoder().encodeToString(imageUrl.getBytes());
                StableDiffusionResponseDto stableDiffusionResponseDto =
                        new StableDiffusionResponseDto(imageUrl, base64EncodedUrl, time, uploadedImageUrl);
                return ResponseEntity.ok(stableDiffusionResponseDto);
            }

            else {
                return ResponseEntity.status(500).body(new StableDiffusionResponseDto(null,"image error",-1.0,null));
            }
        } catch (Exception e) {
            LoggingUtil.logError("SDAPI Exception : ",e);
            return ResponseEntity.status(500).body(new StableDiffusionResponseDto(
                    null, "예외 발생: "+e,-1.0,null));
        }
    }
}
