package com.omori.literalura.repository;

import com.omori.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNome(String nome);
    List<Autor> findByVivoAndAnoNascimentoLessThanEqual(boolean vivo, int ano);
}