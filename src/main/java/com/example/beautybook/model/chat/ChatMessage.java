package com.example.beautybook.model.chat;

import com.example.beautybook.annotetion.OnlyIdToString;
import com.example.beautybook.model.BaseModel;
import com.example.beautybook.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatMessage extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @OnlyIdToString
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @OnlyIdToString
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    private String messageText;
    private LocalDateTime sentAt;

    @Override
    public String toString() {
        return super.toString(this);
    }
}
