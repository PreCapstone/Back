package com.springboot.apiserver.ppurio.service;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.apiserver.ppurio.config.PpurioConfig;
import com.springboot.apiserver.ppurio.ppuriodto.PpurioFileDto;
import com.springboot.apiserver.ppurio.ppuriodto.PpurioMMSRequestDto;
import com.springboot.apiserver.ppurio.ppuriodto.PpurioSMSRequestDto;
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

    public String sendMessage(PpurioSMSRequestDto ppurioSMSRequestDto) throws IOException {
//        만약 기존 발급된 토큰이 있었다면?? 관련 로직 필요
        if(ppurioConfig.getToken()==null){
            requestToken();
        }
        String bearerAuthorization = String.format("%s %s", "Bearer", ppurioConfig.getToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerAuthorization);
//        Map<String, Object> sendParams = createSendTestParams();

        ppurioSMSRequestDto.setAccount(ppurioConfig.getPpurioid());
        ppurioSMSRequestDto.setFrom(ppurioConfig.getPpruioFrom());
        ppurioSMSRequestDto.setMessageType("SMS");
        Map<String,Object> sendParams = ppurioSMSRequestDto.toMap();
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(sendParams,headers);
        System.out.println(sendParams.toString());
//        sendParams.put("files", List.of(null));
        String response = restTemplate.postForObject(ppurioConfig.getPpurioUrl()+"/v1/message", entity, String.class);
        return response;
    }
    public String sendMessage(PpurioMMSRequestDto ppurioMMSRequestDto)throws IOException{
        if(ppurioConfig.getToken()==null){
            requestToken();
        }
        String bearerAuthorization = String.format("%s %s", "Bearer", ppurioConfig.getToken());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerAuthorization);
//        Map<String, Object> sendParams = createSendTestParams();

        ppurioMMSRequestDto.setAccount(ppurioConfig.getPpurioid());
        ppurioMMSRequestDto.setFrom(ppurioConfig.getPpruioFrom());
        ppurioMMSRequestDto.setMessageType("MMS");
        printDecodedFileData(ppurioMMSRequestDto);
        Map<String,Object> sendParams = ppurioMMSRequestDto.toMap();
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(sendParams,headers);
        System.out.println(sendParams.toString());
        String response = restTemplate.postForObject(ppurioConfig.getPpurioUrl()+"/v1/message", entity, String.class);
        return response;
    }
    public void printDecodedFileData(PpurioMMSRequestDto requestDto) {
        List<PpurioFileDto> files = requestDto.getFiles();

        if (files != null && !files.isEmpty()) {
            for (PpurioFileDto file : files) {
                String base64Data = file.getData();
                try {
                    // Base64 디코딩
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
                    String decodedString = new String(decodedBytes);

                    // 디코딩된 데이터 콘솔에 출력
                    System.out.println("Decoded Data for file " + file.getName() + ": " + decodedString);
                } catch (IllegalArgumentException e) {
                    // Base64 디코딩 오류 처리
                    System.err.println("Invalid Base64 data for file: " + file.getName());
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No files to decode.");
        }
    }

//    private Map<String,Object> createSendParams() throws IOException{
//        PpurioVo ppruioVo = new PpurioVo();
//    }

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
