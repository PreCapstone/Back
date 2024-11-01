package com.springboot.apiserver.ppurio.ppuriodto;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PpurioSMSRequestDto {
    private String account;
    private String messageType;
    private String content;
    private String from;
    private String duplicateFlag;
    private int targetCount;
    private List<PpurioTargetDto> targets;
    private String refKey = "SMS_REF_KEY";
//    private String rejectType;
//    private String sendTime;
//    private String subject;
//    private List<PpurioFileDto> files = new ArrayList<>();

    public Map<String, Object> toMap() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account",account);
        params.put("from",from);
        params.put("messageType", messageType);
        params.put("content", content);
        params.put("duplicateFlag", duplicateFlag);
        params.put("targetCount", targetCount);
        params.put("targets", targets);
        params.put("refKey", refKey);
        return params;
    }
}

