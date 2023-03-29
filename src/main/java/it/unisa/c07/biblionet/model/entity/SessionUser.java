package it.unisa.c07.biblionet.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="session_user",schema = "public")
@Getter
@Setter
public class SessionUser implements Serializable {
    @Id
    @Column(name="email")
    String email;

    @Column(name="token")
    String token;

}
