package com.birthday.chb.controller;

import com.birthday.chb.service.GuestbookService;
import com.birthday.chb.dto.GuestbookMessageDto;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173") // Vue 개발 서버 포트
public class GuestbookController {

    private final GuestbookService service;

    public GuestbookController(GuestbookService service) {
        this.service = service;
    }

    @GetMapping
    public List<GuestbookMessageDto.Response> getMessages() {
        return service.getAllMessages();
    }

    @PostMapping
    public GuestbookMessageDto.Response addMessage(@RequestBody GuestbookMessageDto.Request requestDto) {
        return service.saveMessage(requestDto);
    }
}
