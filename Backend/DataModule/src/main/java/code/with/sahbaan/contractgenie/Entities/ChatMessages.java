package code.with.sahbaan.contractgenie.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHAT_MESSAGES")
@Getter
@Setter
public class ChatMessages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CHAT_MESSAGE_ID")
    private long chatMessageId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "USER_MESSAGE", length = 65535)
    private String userMessage;

    @Column(name = "AI_REPLY", length = 65535)
    private String aiReply;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
}
