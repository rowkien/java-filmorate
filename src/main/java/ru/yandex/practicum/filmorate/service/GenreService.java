package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreDao genreDao;

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    public Genre getGenre(int id) {
        if (id > 6 || id < 1) {
            throw new NotFoundException("Такого жанра нет!");
        }
        return genreDao.getGenre(id);
    }
}
