package com.example.beautybook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TelegramCode extends BaseModel {
    @Id
    @Column(name = "code", nullable = false, unique = true)
    private Long code;
    private Long chatId;
    private String userName;
    private String userUuid;
    private Operation operation;

    public enum Operation {
        VERIFICATION,
        LOGIN
    }

    @Override
    public String toString() {
        return super.toString(this);
    }
}
