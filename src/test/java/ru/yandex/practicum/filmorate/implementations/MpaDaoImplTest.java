package ru.yandex.practicum.filmorate.implementations;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDaoImplTest {

    private final MpaDaoImpl mpaDao;

    @Test
    public void shouldCorrectlyGetMpa() {
        Mpa mpa = new Mpa(1, "G");
        Mpa returnedMpa = mpaDao.getMpa(mpa.getId());
        Assertions.assertEquals(mpa, returnedMpa);
    }

    @Test
    public void shouldCorrectlyGetAllMpa() {
        List<Mpa> returnedAllMpa = mpaDao.getAllMpa();
        Assertions.assertEquals(returnedAllMpa.get(2), new Mpa(3, "PG-13"));
        Assertions.assertEquals(5, returnedAllMpa.size());
    }

}