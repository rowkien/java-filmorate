package ru.yandex.practicum.filmorate.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sql = "select * from genre";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "select * from genre where genre_id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new GenreMapper()).
                stream()
                .findAny()
                .orElse(null);
    }
}
