package com.pravin.job_portal_backend.mapper;

import com.pravin.job_portal_backend.dto.MessageDto;
import com.pravin.job_portal_backend.entity.Message;

public class MessageMapper {

  private MessageMapper() {
  }

  public static MessageDto toDto(Message message) {
    if (message == null) {
      return null;
    }
    return new MessageDto(
        message.getId(),
        message.getSender().getId(),
        message.getSender().getName(),
        message.getReceiver().getId(),
        message.getReceiver().getName(),
        message.getContent(),
        message.getSentAt(),
        message.isRead());
  }
}
