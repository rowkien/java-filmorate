package ru.yandex.practicum.filmorate.controller;

import com.google.gson.*;
import org.junit.jupiter.api.*;

import ru.yandex.practicum.filmorate.adapters.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {


    @Order(1)
    @Test
    public void shouldCreateUser() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        String json = gson.toJson(user);
        URI uri = URI.create("http://localhost:8080/users");
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
        User returnedUser = gson.fromJson(returned, User.class);
        Assertions.assertEquals(returnedUser, user);
    }

    @Order(2)
    @Test
    public void shouldGetAllUsers() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/users");
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
    public void shouldUpdateUserWithNewLogin() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(1);
        user.setLogin("updatedLogin");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        String json = gson.toJson(user);
        URI uri = URI.create("http://localhost:8080/users");
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
        User returnedUser = gson.fromJson(returned, User.class);
        Assertions.assertEquals(user.getLogin(), returnedUser.getLogin());
    }

    @Order(4)
    @Test
    public void shouldNotCreateUserWithEmptyLogin() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(1);
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        String json = gson.toJson(user);
        URI uri = URI.create("http://localhost:8080/users");
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
        User returnedUser = gson.fromJson(returned, User.class);
        Assertions.assertNull(returnedUser.getLogin());
    }

    @Order(5)
    @Test
    public void shouldNotCreateUserWithBadLogin() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(1);
        user.setLogin("bad login");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        String json = gson.toJson(user);
        URI uri = URI.create("http://localhost:8080/users");
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
        User returnedUser = gson.fromJson(returned, User.class);
        Assertions.assertNull(returnedUser.getLogin());
    }

    @Order(6)
    @Test
    public void shouldNotCreateUserWithEmptyEmail() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(1);
        user.setName("Nikita");
        user.setLogin("rowkien");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        String json = gson.toJson(user);
        URI uri = URI.create("http://localhost:8080/users");
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
        User returnedUser = gson.fromJson(returned, User.class);
        Assertions.assertNull(returnedUser.getEmail());
    }

    @Order(7)
    @Test
    public void shouldNotCreateUserIfEmailNotContainsAT() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkienyandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        String json = gson.toJson(user);
        URI uri = URI.create("http://localhost:8080/users");
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
        User returnedUser = gson.fromJson(returned, User.class);
        Assertions.assertNull(returnedUser.getEmail());
    }

    @Order(8)
    @Test
    public void shouldSetNameAsLoginIfNameIsEmpty() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkien@yandex.ru");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        String json = gson.toJson(user);
        URI uri = URI.create("http://localhost:8080/users");
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
        User returnedUser = gson.fromJson(returned, User.class);
        Assertions.assertEquals(returnedUser.getName(), user.getLogin());
    }

    @Order(9)
    @Test
    public void shouldNotCreateUserIfBirthdayInFuture() throws IOException, InterruptedException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(3000, 5, 19));
        String json = gson.toJson(user);
        URI uri = URI.create("http://localhost:8080/users");
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
        User returnedUser = gson.fromJson(returned, User.class);
        Assertions.assertNull(returnedUser.getBirthday());
    }
}



