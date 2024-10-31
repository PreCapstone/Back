package com.springboot.apiserver.ppurio.controller;

import com.springboot.apiserver.ppurio.service.PpurioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ppurio/message")
public class PpurioController {
    private final PpurioService ppurioService;

    @Autowired
    public PpurioController(PpurioService ppurioService) {
        this.ppurioService = ppurioService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        String token = ppurioService.requestToken();

        return ResponseEntity.ok(token);
    }

    @GetMapping("/send-test")
    public ResponseEntity<String> sendTest() {
        String response = null;
        try {
            response = ppurioService.sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }
}
