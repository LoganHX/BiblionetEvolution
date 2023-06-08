package it.unisa.c07.biblionet.config;

import it.unisa.c07.biblionet.model.entity.utente.UtenteRegistrato;
import java.util.Map;

public interface JwtGeneratorInterface {
    Map<String, String> generateToken(UtenteRegistrato user);
}