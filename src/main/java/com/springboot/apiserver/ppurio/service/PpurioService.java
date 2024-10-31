package com.springboot.apiserver.ppurio.service;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.apiserver.ppurio.config.PpurioConfig;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;

@AllArgsConstructor
@Service
public class PpurioService {
    private final PpurioConfig ppurioConfig;
    private final RestTemplate restTemplate= new RestTemplate();
    public String requestToken(){
        String token = null;
        String authString = ppurioConfig.getPpurioid() + ":" + ppurioConfig.getPpruioApikey();
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

        String authorizationHeader = "Basic " + encodedAuthString;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authorizationHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        System.out.println(ppurioConfig.getPpurioUrl()+"/v1/token");
        System.out.println(headers);
        String response = restTemplate.postForObject(ppurioConfig.getPpurioUrl()+"/v1/token", entity, String.class);
        System.out.println(response);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            token = rootNode.get("token").asText();
            ppurioConfig.setToken(token);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public String sendMessage() throws IOException {
        if(ppurioConfig.getToken()==null){
            requestToken();
        }
        String bearerAuthorization = String.format("%s %s", "Bearer", ppurioConfig.getToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerAuthorization);

        Map<String, Object> sendParams = createSendTestParams();
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(sendParams,headers);

        String response = restTemplate.postForObject(ppurioConfig.getPpurioUrl()+"/v1/message", entity, String.class);
        return response;
    }

    private Map<String, Object> createSendTestParams() throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", ppurioConfig.getPpurioid());
        params.put("messageType", "SMS");
        params.put("from", ppurioConfig.getPpruioFrom());
        params.put("content", "[*이름*], hello this is [*1*]");
        params.put("duplicateFlag", "Y");
//        params.put("rejectType", "AD"); // 광고성 문자 수신거부 설정, 비활성화할 경우 해당 파라미터 제외
        params.put("targetCount", 1);
        params.put("targets", List.of(
                Map.of("to", "01062224268",
                        "name", "tester",
                        "changeWord", Map.of(
                                "var1", "ppurio api world")))
        );
//        params.put("files", List.of(
//                createFileTestParams(FILE_PATH)
//        ));
        params.put("refKey", "ABCDEFG"); // refKey 생성, 32자 이내로 아무 값이든 상관 없음
        return params;
    }

}
