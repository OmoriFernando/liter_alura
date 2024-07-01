package com.omori.literalura.service;

import com.omori.literalura.model.Autor;
import com.omori.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosNoAno(int ano) {
        return autorRepository.findByVivoAndAnoNascimentoLessThanEqual(true, ano);
    }

    public Autor buscarOuCriarAutor(String nomeAutor) {
        return autorRepository.findByNome(nomeAutor)
                .orElseGet(() -> {
                    Autor novoAutor = new Autor();
                    novoAutor.setNome(nomeAutor);
                    // Defina outros atributos padrão para o novo autor, se necessário
                    return autorRepository.save(novoAutor);
                });
    }
}
