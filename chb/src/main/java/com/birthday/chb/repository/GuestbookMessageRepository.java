package com.birthday.chb.repository;

import com.birthday.chb.domain.GuestbookMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GuestbookMessageRepository extends JpaRepository<GuestbookMessage, Long> {
    // 생성 시간의 내림차순으로 모든 메시지를 찾습니다.
    List<GuestbookMessage> findAllByOrderByCreatedAtDesc();
}
