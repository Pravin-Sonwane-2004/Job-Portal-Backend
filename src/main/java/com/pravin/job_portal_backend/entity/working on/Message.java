// package com.pravin.job_portal_backend.entity;

// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;

// import java.time.LocalDateTime;

// @Entity
// @Table(name = "messages")
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// public class Message {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "sender_id", nullable = false)
//     private User sender;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "receiver_id", nullable = false)
//     private User receiver;

//     @Column(nullable = false, length = 2000)
//     private String content;

//     @Column(name = "sent_at", nullable = false)
//     private LocalDateTime sentAt = LocalDateTime.now();

//     @Column(nullable = false)
//     private boolean read = false;

// }
