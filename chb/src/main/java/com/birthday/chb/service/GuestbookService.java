package com.birthday.chb.service;

import com.birthday.chb.domain.GuestbookMessage;
import com.birthday.chb.dto.GuestbookMessageDto;
import com.birthday.chb.repository.GuestbookMessageRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestbookService {

    private final GuestbookMessageRepository repository;

    public GuestbookService(GuestbookMessageRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<GuestbookMessageDto.Response> getAllMessages() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(GuestbookMessageDto.Response::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public GuestbookMessageDto.Response saveMessage(GuestbookMessageDto.Request requestDto) {
        return new GuestbookMessageDto.Response(repository.save(requestDto.toEntity()));
    }

    @Transactional
    public GuestbookMessage likeMessage(Long id) {
        GuestbookMessage message = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
        message.setLikes(message.getLikes() + 1);
        return repository.save(message);
    }
}