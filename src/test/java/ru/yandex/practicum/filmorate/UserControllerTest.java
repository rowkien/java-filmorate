package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {

    UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }


    @Test
    public void shouldCreateUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        User createdUser = userController.createUser(user);
        Assertions.assertEquals(user, createdUser);
    }

    @Test
    public void shouldGetAllUsers() {
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        userController.createUser(user);
        Assertions.assertEquals(user, userController.getAllUsers().get(0));
    }

    @Test
    public void shouldUpdateUserWithNewLogin() {
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        userController.createUser(user);
        user.setLogin("updated");
        User updatedUser = userController.updateUser(user);
        Assertions.assertEquals(updatedUser.getLogin(), "updated");
    }

    @Test
    public void shouldNotCreateUserWithEmptyLogin() {
        User user = new User();
        user.setId(1);
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Логин не может быть пустым и содержать пробелы!");
        }
    }

    @Test
    public void shouldNotCreateUserWithBadLogin() {
        User user = new User();
        user.setId(1);
        user.setLogin("bad login");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Логин не может быть пустым и содержать пробелы!");
        }
    }

    @Test
    public void shouldNotCreateUserWithEmptyEmail() {
        User user = new User();
        user.setId(1);
        user.setName("Nikita");
        user.setLogin("rowkien");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Электронная почта не может быть пустой и должна содержать символ @!");
        }
    }

    @Test
    public void shouldNotCreateUserIfEmailNotContainsAT() {
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkienyandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Электронная почта не может быть пустой и должна содержать символ @!");
        }
    }

    @Test
    public void shouldSetNameAsLoginIfNameIsEmpty() {
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkien@yandex.ru");
        user.setBirthday(LocalDate.of(1997, 5, 19));
        userController.createUser(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void shouldNotCreateUserIfBirthdayInFuture() {
        User user = new User();
        user.setId(1);
        user.setLogin("rowkien");
        user.setEmail("rowkien@yandex.ru");
        user.setName("Nikita");
        user.setBirthday(LocalDate.of(3000, 5, 19));
        try {
            userController.createUser(user);
        } catch (ValidationException exception) {
            Assertions.assertEquals(exception.getMessage(), "Дата рождения не может быть пустой или в будущем!");
        }
    }
}



