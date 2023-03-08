package com.example.webapp.repository;

import com.example.webapp.entity.User;
import com.example.webapp.exeption.UserNotFoundException;
import com.example.webapp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final String SQL_FIND_USER_BY_EMAIL =
            "select id, name, email, password from users where email = :email";
    private static final String SQL_FIND_USER_BY_ID =
            "select id, name, email, password from users where id = :id";
    private static final String SQL_INSERT_USER = "insert into users(name, email, password) values (:name, :email, :password)";
    private static final String SQL_FIND_USERS = "select * from users";

    private final UserMapper userMapper;
    @Autowired
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(
            UserMapper userMapper,
            NamedParameterJdbcTemplate jdbcTemplate
    ) {
        this.userMapper = userMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> authenticate(String email, String password) throws UserNotFoundException {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<User> user = this.findUserByEmail(email);
        if (encoder.matches(password, user.get().getPassword())) {
            return user;
        } else {
            throw new UserNotFoundException();
        }
    }


    public User saveUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource();

        params.addValue("name", user.getName());
        params.addValue("email", user.getEmail());

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        params.addValue("password", encodedPassword);

        jdbcTemplate.update(SQL_INSERT_USER, params, keyHolder, new String[] { "id" });
        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    public List<User> findAllUsers() {
        List<User> users = jdbcTemplate.query(
                SQL_FIND_USERS,
                this.userMapper);
        return users;
    }

    public Optional<User> findUserById(int id) {
        var params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbcTemplate.query(SQL_FIND_USER_BY_ID, params, this.userMapper).stream().findFirst();
    }

    public Optional<User> findUserByEmail(String email) {
        var params = new MapSqlParameterSource();
        params.addValue("email", email);

        return jdbcTemplate.query(SQL_FIND_USER_BY_EMAIL, params, this.userMapper).stream().findFirst();
    }
}
