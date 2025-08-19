// package com.pravin.job_portal_backend.service.service_implementation;

// import com.pravin.job_portal_backend.entity.Message;
// import com.pravin.job_portal_backend.entity.JOB_SEEKER;
// import com.pravin.job_portal_backend.repository.MessageRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class MessageService {
//     @Autowired
//     private MessageRepository messageRepository;

//     public List<Message> getMessagesBySender(JOB_SEEKER sender) {
//         return messageRepository.findBySender(sender);
//     }

//     public List<Message> getMessagesByReceiver(JOB_SEEKER receiver) {
//         return messageRepository.findByReceiver(receiver);
//     }

//     public List<Message> getConversation(JOB_SEEKER sender, JOB_SEEKER receiver) {
//         return messageRepository.findBySenderAndReceiver(sender, receiver);
//     }

//     public Message sendMessage(JOB_SEEKER sender, JOB_SEEKER receiver, String content) {
//         return messageRepository.save(new Message(sender, receiver, content));
//     }

//     public void deleteMessage(Long messageId) {
//         messageRepository.deleteById(messageId);
//     }
// }
