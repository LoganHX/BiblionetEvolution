package it.unisa.c07.biblionet.registrazione.controller;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRegister {

    private String userId;
    private String password;
    private String role;
    private String email;

    private String provincia;
    private String citta;
    private String via;
    private String recapitoTelefonico;

    private String nome;
    private String cognome;

    private String username;
    private String emailBiblioteca;

}
