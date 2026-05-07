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
    MessageDto dto = new MessageDto();
    dto.setId(message.getId());
    dto.setSenderId(message.getSender().getId());
    dto.setSenderName(message.getSender().getName());
    dto.setReceiverId(message.getReceiver().getId());
    dto.setReceiverName(message.getReceiver().getName());
    dto.setContent(message.getContent());
    dto.setSentAt(message.getSentAt());
    dto.setRead(message.isRead());
    return dto;
  }
}
