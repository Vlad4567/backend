package com.example.beautybook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class AuthenticationFailureLog extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ip;
    private String token;
    private LocalDateTime date;
    private int loginFails;
    private LocalDateTime blockEndTime;

    public AuthenticationFailureLog(String ip, String token, LocalDateTime date) {
        this.ip = ip;
        this.token = token;
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString(this);
    }
}
