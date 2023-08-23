package ru.yandex.practicum.filmorate.implementations;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoImplTest {

    private final GenreDaoImpl genreDao;

    @Test
    public void shouldCorrectGetGenre() {
        Genre genre = new Genre(1, "Комедия");
        Genre returnedGenre = genreDao.getGenre(genre.getId());
        Assertions.assertEquals(genre, returnedGenre);
    }

    @Test
    public void shouldCorrectGetAllGenres() {
        List<Genre> genres = genreDao.getAllGenres();
        Assertions.assertEquals(genres.get(2), new Genre(3, "Мультфильм"));
        Assertions.assertEquals(6, genres.size());
    }

}