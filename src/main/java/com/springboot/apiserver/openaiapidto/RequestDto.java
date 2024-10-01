package com.springboot.apiserver.openaiapidto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestDto {
    private String model;
    private List<MessageDto> messages;

    public RequestDto(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<MessageDto>();
        this.messages.add(new MessageDto("user",prompt));
    }
}
