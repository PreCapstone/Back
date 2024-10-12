package com.springboot.apiserver.sd.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class StableDiffusionItoIConfig {
    @Value("${sd.api.key}")
    private String sdApiKey;
    @Bean(name="sdRestTemplate")//OpenAI RestTemplate이랑 중복방지 이름설정
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getSdApiKey() {
        return sdApiKey;
    }
}
