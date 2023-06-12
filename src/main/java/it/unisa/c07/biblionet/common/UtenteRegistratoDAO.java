package it.unisa.c07.biblionet.common;

import it.unisa.c07.biblionet.common.UtenteRegistrato;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Questa classe rappresenta il DAO di un Utente Registrato, usato
 * per estendere gli attori core del sistema.
 */
@Primary
public interface UtenteRegistratoDAO
        extends JpaRepository<UtenteRegistrato, String> {
    UtenteRegistrato findByEmailAndPassword(String email, byte[] password);
    UtenteRegistrato findByEmail(String email);

}
