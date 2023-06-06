package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.adapters.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class FilmControllerTest {

    @Order(1)
    @Test
    public void shouldCreateFilm() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(152);
        String json = gson.toJson(film);
        URI uri = URI.create("http://localhost:8080/films");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = requestBuilder
                .POST(body)
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        String returned = response.body();
        Film returnedFilm = gson.fromJson(returned, Film.class);
        Assertions.assertEquals(returnedFilm, film);
    }

    @Order(2)
    @Test
    public void shouldGetAllFilms() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/films");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder
                .GET()
                .uri(uri)
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        String body = response.body();
        JsonElement json = JsonParser.parseString(body);
        if (json.isJsonArray() && !json.isJsonNull()) {
            JsonArray jsonArray = json.getAsJsonArray();
            Assertions.assertEquals(1, jsonArray.size());
        }
    }

    @Order(3)
    @Test
    public void shouldUpdateFilmWithNewDescription() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("MOST EPIC ever DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(152);
        String json = gson.toJson(film);
        URI uri = URI.create("http://localhost:8080/films");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = requestBuilder
                .PUT(body)
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        String returned = response.body();
        Film returnedFilm = gson.fromJson(returned, Film.class);
        Assertions.assertEquals(film.getDescription(), returnedFilm.getDescription());
    }

    @Order(4)
    @Test
    public void shouldNotCreateFilmWithEmptyName() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        Film film = new Film();
        film.setId(1);
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(152);
        String json = gson.toJson(film);
        URI uri = URI.create("http://localhost:8080/films");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = requestBuilder
                .POST(body)
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        String returned = response.body();
        Film returnedFilm = gson.fromJson(returned, Film.class);
        Assertions.assertNull(returnedFilm.getName());
    }

    @Order(5)
    @Test
    public void shouldNotCreateFilmWithBadDescription() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("qwerqwerqwerqwerqwerqwerqwerqwerqqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqw" +
                "qwerqwerqwerqwerqwerqwerqwerqwereqwrqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqw" +
                "qwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerqwerq");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(152);
        String json = gson.toJson(film);
        URI uri = URI.create("http://localhost:8080/films");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = requestBuilder
                .POST(body)
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        String returned = response.body();
        Film returnedFilm = gson.fromJson(returned, Film.class);
        Assertions.assertNull(returnedFilm.getDescription());
    }

    @Order(6)
    @Test
    public void shouldNotCreateFilmWithBadReleaseDate() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(152);
        String json = gson.toJson(film);
        URI uri = URI.create("http://localhost:8080/films");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = requestBuilder
                .POST(body)
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        String returned = response.body();
        Film returnedFilm = gson.fromJson(returned, Film.class);
        Assertions.assertNull(returnedFilm.getReleaseDate());
    }

    @Order(7)
    @Test
    public void shouldNotCreateFilmWithBadDuration() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        Film film = new Film();
        film.setId(1);
        film.setName("Batman");
        film.setDescription("DC comics movie");
        film.setReleaseDate(LocalDate.of(2008, 7, 18));
        film.setDuration(-152);
        String json = gson.toJson(film);
        URI uri = URI.create("http://localhost:8080/films");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = requestBuilder
                .POST(body)
                .uri(uri)
                .header("Content-Type", "application/json")
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        String returned = response.body();
        Film returnedFilm = gson.fromJson(returned, Film.class);
        Assertions.assertEquals(0, returnedFilm.getDuration());
    }

}