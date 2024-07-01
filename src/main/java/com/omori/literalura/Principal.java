package com.omori.literalura;

import com.omori.literalura.model.Autor;
import com.omori.literalura.model.Livro;
import com.omori.literalura.service.AutorService;
import com.omori.literalura.service.GutendexService;
import com.omori.literalura.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class Principal {

    private final Scanner scanner = new Scanner(System.in);
    private final LivroService livroService;
    private final AutorService autorService;
    private final GutendexService gutendexService;

    @Autowired
    public Principal(LivroService livroService, AutorService autorService, GutendexService gutendexService) {
        this.livroService = livroService;
        this.autorService = autorService;
        this.gutendexService = gutendexService;
    }

    public void exibeMenu() {
        while (true) {
            System.out.println("\nEscolha o número de sua opção:");
            System.out.println("1- Buscar livro pelo título");
            System.out.println("2- Listar livros registrados");
            System.out.println("3- Listar autores registrados");
            System.out.println("4- Listar autores vivos em um determinado ano");
            System.out.println("5- Listar livros em um determinado idioma");
            System.out.println("0- Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer do teclado

            if (opcao == 0) {
                System.out.println("Saindo...");
                break;
            }

            switch (opcao) {
                case 1:
                    buscarLivroPorTitulo();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresPorAno();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void buscarLivroPorTitulo() {
        System.out.print("Digite o título do livro: ");
        String titulo = scanner.nextLine();

        try {
            List<Livro> livrosEncontrados = gutendexService.buscarLivroPorTitulo(titulo);

            if (livrosEncontrados.isEmpty()) {
                System.out.println("Nenhum livro encontrado com o título: " + titulo);
            } else {
                System.out.println("Livros encontrados:");
                livrosEncontrados.forEach(this::exibirLivro);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar livro: " + e.getMessage());
            // Aqui você pode adicionar um tratamento mais específico se necessário.
        }
    }


    private void listarLivros() {
        List<Livro> livros = livroService.listarLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado.");
        } else {
            System.out.println("Livros registrados:");
            livros.forEach(this::exibirLivro);
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorService.listarAutores();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado.");
        } else {
            System.out.println("Autores registrados:");
            autores.forEach(this::exibirAutor);
        }
    }

    private void listarAutoresPorAno() {
        System.out.print("Digite o ano: ");
        int ano = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer do teclado

        List<Autor> autores = autorService.listarAutoresVivosNoAno(ano);
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo em " + ano + ".");
        } else {
            System.out.println("Autores vivos em " + ano + ":");
            autores.forEach(this::exibirAutor);
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.print("Digite o idioma (PT, EN, ES, FR): ");
        String idioma = scanner.nextLine().toUpperCase();

        List<Livro> livros = livroService.listarLivrosPorIdioma(idioma);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado em " + idioma + ".");
        } else {
            System.out.println("Livros em " + idioma + ":");
            livros.forEach(this::exibirLivro);
        }
    }

    private void exibirLivro(Livro livro) {
        System.out.println("Título: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor().getNome());
        System.out.println("Idioma: " + livro.getIdioma());
        System.out.println("Número de Downloads: " + livro.getNumeroDownloads());
        System.out.println("-----------------------");
    }

    private void exibirAutor(Autor autor) {
        System.out.println("Nome: " + autor.getNome());
        System.out.println("Ano de Nascimento: " + autor.getAnoNascimento());
        if (autor.getAnoFalecimento() != null) {
            System.out.println("Ano de Falecimento: " + autor.getAnoFalecimento());
        } else {
            System.out.println("Ano de Falecimento: - ");
        }
        System.out.println("-----------------------");
    }
}
