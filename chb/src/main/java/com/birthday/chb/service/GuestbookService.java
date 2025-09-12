package com.birthday.chb.service;

import com.birthday.chb.domain.GuestbookMessage;
import com.birthday.chb.dto.GuestbookMessageDto;
import com.birthday.chb.repository.GuestbookMessageRepository;

import jakarta.persistence.EntityNotFoundException;

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
    public GuestbookMessageDto.Response likeMessage(Long id) {
        GuestbookMessage message = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + id));
        message.setLikes(message.getLikes() + 1);
        return new GuestbookMessageDto.Response(repository.save(message));
    }

    @Transactional
    public void deleteMessage(Long id, String password) {
        // 메시지가 존재하는지 확인하고, 없으면 예외를 발생시킵니다.
        GuestbookMessage message = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ID " + id + "에 해당하는 메시지를 찾을 수 없습니다."));

        // 비밀번호를 확인합니다. 실제 운영에서는 암호화된 비밀번호를 비교해야 합니다.
        if (message.getPassword() == null || !message.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        repository.deleteById(message.getId());
    }
}