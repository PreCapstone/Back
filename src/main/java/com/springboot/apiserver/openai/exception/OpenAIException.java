package com.springboot.apiserver.openai.exception;

import com.springboot.apiserver.exception.OpenAIInputErrorException;
import com.springboot.apiserver.openai.openaiapidto.RequestDto;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

//메세지 유효성 검사 클래스
@NoArgsConstructor
@Service
public class OpenAIException {
    public void checkRequest(RequestDto requestDto) {
        if (requestDto.getPrompt() == null || requestDto.getPrompt().isEmpty()) {
            throw new OpenAIInputErrorException("prompt가 설정되지 않았습니다.", "EMPTY_PROMPT", HttpStatus.BAD_REQUEST);
        }
        if(requestDto.getTarget() == null || requestDto.getTarget().isEmpty()){
            throw new OpenAIInputErrorException("Target이 설정되지 않았습니다.", "EMPTY_TARGET",HttpStatus.BAD_REQUEST);
        }
        if (requestDto.getMood() == null) {
            throw new OpenAIInputErrorException("mood가 설정되지 않았습니다.", "EMPTY_MOOD",HttpStatus.BAD_REQUEST);
        }
        if(requestDto.getKeyword()==null || requestDto.getKeyword().isEmpty()){
            throw new OpenAIInputErrorException("Keyword가 설정되지 않았습니다.", "EMPTY_KEYWORD",HttpStatus.BAD_REQUEST);
        }
        if(requestDto.getProduct()==null || requestDto.getProduct().isEmpty()){
            throw new OpenAIInputErrorException("Product가 설정되지 않았습니다.","EMPTY_PRODUCT",HttpStatus.BAD_REQUEST);
        }
    }

    public String generateMessage(RequestDto requestDto) {
        return "성공적으로 생성되었습니다.";
    }

}
