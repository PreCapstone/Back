package com.springboot.apiserver.sd.service;

import com.springboot.apiserver.sd.config.StableDiffusionItoIConfig;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class StableDiffusionService {
    private final StableDiffusionItoIConfig sdConfig;
    private final RestTemplate restTemplate;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // 연결 타임아웃
            .writeTimeout(30, TimeUnit.SECONDS)    // 쓰기 타임아웃
            .readTimeout(120, TimeUnit.SECONDS)    // 읽기 타임아웃(긴 작업을 대비)
            .build();

    public StableDiffusionService(@Qualifier("sdRestTemplate") RestTemplate restTemplate, StableDiffusionItoIConfig sdConfig) {
        this.restTemplate = restTemplate;
        this.sdConfig = sdConfig;
    }

    public Response generateImage(String prompt, String initImage) {
        Response response = null;
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\n" +
                        "  \"key\": \"" + sdConfig.getSdApiKey() + "\",\n" +
                        "  \"model_id\": \"realistic-vision-51\",\n" +
                        "  \"prompt\": \"" + prompt + "\",\n" +
                        "  \"negative_prompt\": null,\n" +
                        "  \"init_image\": \"" + initImage + "\",\n" +
                        "  \"samples\": \"1\",\n" +
                        "  \"num_inference_steps\": \"31\",\n" +
                        "  \"safety_checker\": \"yes\",\n" +
                        "  \"enhance_prompt\": \"yes\",\n" +
                        "  \"guidance_scale\": 7.5,\n" +
                        "  \"strength\": 0.7,\n" +
                        "  \"scheduler\": \"UniPCMultistepScheduler\",\n" +
                        "  \"seed\": null,\n" +
                        "  \"lora_model\": null,\n" +
                        "  \"tomesd\": \"yes\",\n" +
                        "  \"use_karras_sigmas\": \"yes\",\n" +
                        "  \"vae\": null,\n" +
                        "  \"lora_strength\": null,\n" +
                        "  \"embeddings_model\": null,\n" +
                        "  \"webhook\": null,\n" +
                        "  \"track_id\": null,\n" +
                        "  \"base64\": \"no\"\n" +
                        "}");

        Request request = new Request.Builder()
                .url(sdConfig.getSdApiURL())
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
