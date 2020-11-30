package com.example.demo.account;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    private String username;
    private String password;
    private String password_repeat;
    private String code;
    private boolean remember;
    private String redirect_url;

    public void generateCode() {
        this.code = UUID.randomUUID().toString();
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }
}
