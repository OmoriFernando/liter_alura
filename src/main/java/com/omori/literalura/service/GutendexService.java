package com.omori.literalura.service;

import com.omori.literalura.model.Autor;
import com.omori.literalura.model.Livro;
import com.omori.literalura.repository.AutorRepository;
import com.omori.literalura.repository.LivroRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GutendexService {
    private static final String GUTENDEX_API_URL = "https://gutendex.com/books?search=";
    private final RestTemplate restTemplate;
    private final LivroService livroService;
    private final AutorService autorService;

    @Autowired
    public GutendexService(RestTemplate restTemplate, LivroService livroService, AutorService autorService) {
        this.restTemplate = restTemplate;
        this.livroService = livroService;
        this.autorService = autorService;
    }

    public List<Livro> buscarLivroPorTitulo(String titulo) {
        String url = GUTENDEX_API_URL + titulo.replace(" ", "%20");
        try {
            String response = restTemplate.getForObject(url, String.class);

            if (response == null) {
                System.err.println("Resposta nula da API Gutendex.");
                return Collections.emptyList();
            }

            JSONObject jsonObject = new JSONObject(response);
            JSONArray results = jsonObject.getJSONArray("results");

            List<Livro> livrosEncontrados = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                JSONObject bookJson = results.getJSONObject(i);
                String tituloLivro = bookJson.getString("title");
                String idiomaLivro = bookJson.getJSONArray("languages").getString(0);
                int downloadsLivro = bookJson.getInt("download_count");

                JSONArray autoresJson = bookJson.getJSONArray("authors");
                if (!autoresJson.isEmpty()) {
                    JSONObject autorJson = autoresJson.getJSONObject(0);
                    String nomeAutor = autorJson.optString("name", "Autor Desconhecido");
                    Autor autor = autorService.buscarOuCriarAutor(nomeAutor);

                    Livro livro = new Livro(tituloLivro, idiomaLivro, 0, downloadsLivro, autor);
                    livroService.salvarLivro(livro);
                    livrosEncontrados.add(livro);
                }
            }
            return livrosEncontrados;

        } catch (RestClientException e) {
            System.err.println("Erro ao consultar a API Gutendex: " + e.getMessage());
            return Collections.emptyList();
        } catch (JSONException e) {
            System.err.println("Erro ao analisar JSON da API Gutendex: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
