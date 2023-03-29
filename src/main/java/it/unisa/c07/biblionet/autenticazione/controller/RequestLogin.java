package it.unisa.c07.biblionet.autenticazione.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLogin {
    String email;
    String password;
}
