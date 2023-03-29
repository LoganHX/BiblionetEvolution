package it.unisa.c07.biblionet.registrazione.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRegister {

    String userId;
    String password;
    String role;

    String email;
}
