package com.birthday.chb.dto;

import com.birthday.chb.domain.GuestbookMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class GuestbookMessageDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        private String author;
        private String message;

        public GuestbookMessage toEntity() {
            GuestbookMessage guestbookMessage = new GuestbookMessage();
            guestbookMessage.setAuthor(this.author);
            guestbookMessage.setMessage(this.message);
            return guestbookMessage;
        }
    }

    @Getter
    public static class Response {
        private final Long id;
        private final String author;
        private final String message;
        private final LocalDateTime createdAt;

        public Response(GuestbookMessage guestbookMessage) {
            this.id = guestbookMessage.getId();
            this.author = guestbookMessage.getAuthor();
            this.message = guestbookMessage.getMessage();
            this.createdAt = guestbookMessage.getCreatedAt();
        }
    }
}