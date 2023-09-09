package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentoDAO extends JpaRepository<Commento, Long> {
    // Eventuali metodi personalizzati se necessario
}
