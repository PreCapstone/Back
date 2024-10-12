package com.springboot.apiserver.sd.service;

import com.springboot.apiserver.sd.config.StableDiffusionItoIConfig;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class StableDiffusionService {
    private final StableDiffusionItoIConfig sdConfig;
    private final RestTemplate restTemplate;

    public StableDiffusionService(@Qualifier("sdRestTemplate") RestTemplate restTemplate, StableDiffusionItoIConfig sdConfig) {
        this.restTemplate = restTemplate;
        this.sdConfig = sdConfig;
    }

    public Response generateImage(String prompt, String initImage) {
        Response response = null;
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\n" +
                        "  \"key\": \"" + sdConfig.getSdApiKey() + "\",\n" +
                        "  \"prompt\": \"" + prompt + "\",\n" +
                        "  \"negative_prompt\": null,\n" +
                        "  \"init_image\": \"" + initImage + "\",\n" +
                        "  \"width\": \"512\",\n" +
                        "  \"height\": \"512\",\n" +
                        "  \"samples\": \"1\",\n" +
                        "  \"num_inference_steps\": \"30\",\n" +
                        "  \"safety_checker\": \"no\",\n" +
                        "  \"enhance_prompt\": \"yes\",\n" +
                        "  \"guidance_scale\": 7.5,\n" +
                        "  \"strength\": 0.7,\n" +
                        "  \"seed\": null,\n" +
                        "  \"webhook\": null,\n" +
                        "  \"track_id\": null\n" +
                        "}");


        Request request = new Request.Builder()
                .url("https://stablediffusionapi.com/api/v3/img2img")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            response = client.newCall(request).execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return response;
        }
}
