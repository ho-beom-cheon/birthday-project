package com.birthday.chb.controller;

import com.birthday.chb.service.GuestbookService;
import com.birthday.chb.domain.GuestbookMessage;
import com.birthday.chb.dto.GuestbookMessageDto;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
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

     // 좋아요 API 엔드포인트 추가
    @PostMapping("/{id}/like")
    public GuestbookMessageDto.Response likeMessage(@PathVariable Long id) {
        return service.likeMessage(id);
    }
}
