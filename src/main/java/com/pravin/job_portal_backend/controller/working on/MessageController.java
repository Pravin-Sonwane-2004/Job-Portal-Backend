// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.entity.Message;
// import com.pravin.job_portal_backend.entity.User;
// import com.pravin.job_portal_backend.repository.UserRepository;
// import com.pravin.job_portal_backend.service.service_implementation.MessageService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api/messages")
// public class MessageController {
//     @Autowired
//     private MessageService messageService;
//     @Autowired
//     private UserRepository userRepository;

//     @GetMapping("/sent/{userId}")
//     public ResponseEntity<List<Message>> getSentMessages(@PathVariable Long userId) {
//         User sender = userRepository.findById(userId).orElseThrow();
//         return ResponseEntity.ok(messageService.getMessagesBySender(sender));
//     }

//     @GetMapping("/received/{userId}")
//     public ResponseEntity<List<Message>> getReceivedMessages(@PathVariable Long userId) {
//         User receiver = userRepository.findById(userId).orElseThrow();
//         return ResponseEntity.ok(messageService.getMessagesByReceiver(receiver));
//     }

//     @GetMapping("/conversation")
//     public ResponseEntity<List<Message>> getConversation(@RequestParam Long senderId, @RequestParam Long receiverId) {
//         User sender = userRepository.findById(senderId).orElseThrow();
//         User receiver = userRepository.findById(receiverId).orElseThrow();
//         return ResponseEntity.ok(messageService.getConversation(sender, receiver));
//     }

//     @PostMapping("/send")
//     public ResponseEntity<Message> sendMessage(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam String content) {
//         User sender = userRepository.findById(senderId).orElseThrow();
//         User receiver = userRepository.findById(receiverId).orElseThrow();
//         return ResponseEntity.ok(messageService.sendMessage(sender, receiver, content));
//     }

//     @DeleteMapping("/delete/{messageId}")
//     public ResponseEntity<?> deleteMessage(@PathVariable Long messageId) {
//         messageService.deleteMessage(messageId);
//         return ResponseEntity.ok().build();
//     }
// }
