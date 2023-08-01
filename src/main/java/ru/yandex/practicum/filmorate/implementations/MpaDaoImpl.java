package ru.yandex.practicum.filmorate.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    @Override
    public Mpa getMpa(int id) {
        Mpa mpa = null;
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE mpa_id = ?", id);
        if (mpaRows.next()) {
            mpa = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("mpa_name")
            );
        }
        return mpa;
    }

}
