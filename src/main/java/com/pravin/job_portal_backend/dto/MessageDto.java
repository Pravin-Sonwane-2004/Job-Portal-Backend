package com.pravin.job_portal_backend.dto;

import java.time.LocalDateTime;

public record MessageDto(
    Long id,
    Long senderId,
    String senderName,
    Long receiverId,
    String receiverName,
    String content,
    LocalDateTime sentAt,
    boolean read) {
}
