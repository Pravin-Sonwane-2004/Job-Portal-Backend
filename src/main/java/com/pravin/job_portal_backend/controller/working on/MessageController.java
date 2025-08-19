// package com.pravin.job_portal_backend.controller;

// import com.pravin.job_portal_backend.entity.Message;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.repository.JOB_SEEKERRepository;
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
//     private JOB_SEEKERRepository JOB_SEEKERRepository;

//     @GetMapping("/sent/{JOB_SEEKERId}")
//     public ResponseEntity<List<Message>> getSentMessages(@PathVariable Long JOB_SEEKERId) {
//         JOB_SEEKER sender = JOB_SEEKERRepository.findById(JOB_SEEKERId).orElseThrow();
//         return ResponseEntity.ok(messageService.getMessagesBySender(sender));
//     }

//     @GetMapping("/received/{JOB_SEEKERId}")
//     public ResponseEntity<List<Message>> getReceivedMessages(@PathVariable Long JOB_SEEKERId) {
//         JOB_SEEKER receiver = JOB_SEEKERRepository.findById(JOB_SEEKERId).orElseThrow();
//         return ResponseEntity.ok(messageService.getMessagesByReceiver(receiver));
//     }

//     @GetMapping("/conversation")
//     public ResponseEntity<List<Message>> getConversation(@RequestParam Long senderId, @RequestParam Long receiverId) {
//         JOB_SEEKER sender = JOB_SEEKERRepository.findById(senderId).orElseThrow();
//         JOB_SEEKER receiver = JOB_SEEKERRepository.findById(receiverId).orElseThrow();
//         return ResponseEntity.ok(messageService.getConversation(sender, receiver));
//     }

//     @PostMapping("/send")
//     public ResponseEntity<Message> sendMessage(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam String content) {
//         JOB_SEEKER sender = JOB_SEEKERRepository.findById(senderId).orElseThrow();
//         JOB_SEEKER receiver = JOB_SEEKERRepository.findById(receiverId).orElseThrow();
//         return ResponseEntity.ok(messageService.sendMessage(sender, receiver, content));
//     }

//     @DeleteMapping("/delete/{messageId}")
//     public ResponseEntity<?> deleteMessage(@PathVariable Long messageId) {
//         messageService.deleteMessage(messageId);
//         return ResponseEntity.ok().build();
//     }
// }
