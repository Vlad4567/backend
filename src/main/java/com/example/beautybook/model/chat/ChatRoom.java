package com.example.beautybook.model.chat;

import com.example.beautybook.annotetion.IgnoreToSting;
import com.example.beautybook.model.BaseModel;
import com.example.beautybook.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@SQLRestriction("is_deleted=false")
public class ChatRoom extends BaseModel {
    @Id
    private Long id;

    @ManyToMany
    @IgnoreToSting
    @JoinTable(
            name = "chatroom_users",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @Column(
            name = "is_deleted",
            nullable = false,
            columnDefinition = "BOOLEAN DEFAULT false"
    )
    private boolean isDeleted;

    @Override
    public String toString() {
        return super.toString(this);
    }
}
