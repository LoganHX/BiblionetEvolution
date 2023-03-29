package it.unisa.c07.biblionet.model.dao;

import it.unisa.c07.biblionet.model.entity.SessionUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionUserDao extends JpaRepository <SessionUser,String> {
}
