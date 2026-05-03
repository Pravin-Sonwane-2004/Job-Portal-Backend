package com.pravin.job_portal_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pravin.job_portal_backend.dto.MessageDto;
import com.pravin.job_portal_backend.dto.SendMessageRequest;
import com.pravin.job_portal_backend.entity.Message;
import com.pravin.job_portal_backend.entity.User;
import com.pravin.job_portal_backend.repository.MessageRepository;
import com.pravin.job_portal_backend.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  public MessageController(MessageRepository messageRepository, UserRepository userRepository) {
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
  }

  @GetMapping("/inbox")
  public ResponseEntity<List<MessageDto>> inbox(Authentication authentication) {
    User user = userFor(authentication.getName());
    return ResponseEntity.ok(messageRepository.findByReceiverOrderBySentAtDesc(user).stream().map(this::toDto).toList());
  }

  @GetMapping("/sent")
  public ResponseEntity<List<MessageDto>> sent(Authentication authentication) {
    User user = userFor(authentication.getName());
    return ResponseEntity.ok(messageRepository.findBySenderOrderBySentAtDesc(user).stream().map(this::toDto).toList());
  }

  @GetMapping("/conversation")
  public ResponseEntity<List<MessageDto>> conversation(Authentication authentication, @RequestParam Long userId) {
    User current = userFor(authentication.getName());
    User other = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found."));
    return ResponseEntity.ok(messageRepository
        .findBySenderAndReceiverOrReceiverAndSenderOrderBySentAtAsc(current, other, current, other)
        .stream().map(this::toDto).toList());
  }

  @PostMapping
  public ResponseEntity<MessageDto> send(Authentication authentication, @Valid @RequestBody SendMessageRequest request) {
    User sender = userFor(authentication.getName());
    User receiver = userRepository.findById(request.receiverId())
        .orElseThrow(() -> new IllegalArgumentException("Receiver not found."));
    Message message = Message.builder().sender(sender).receiver(receiver).content(request.content()).build();
    return ResponseEntity.status(HttpStatus.CREATED).body(toDto(messageRepository.save(message)));
  }

  private User userFor(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found."));
  }

  private MessageDto toDto(Message message) {
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
