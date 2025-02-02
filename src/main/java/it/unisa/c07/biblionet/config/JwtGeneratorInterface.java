package it.unisa.c07.biblionet.config;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import java.util.Map;

public interface JwtGeneratorInterface {
    Map<String, String> generateToken(UtenteRegistrato user);
}