package it.unisa.c07.biblionet.gestioneclubdellibro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDAO extends JpaRepository<Post, Integer> {

    List<Post> findPostByClubDelLibro_IdClub(int idClub);
}
