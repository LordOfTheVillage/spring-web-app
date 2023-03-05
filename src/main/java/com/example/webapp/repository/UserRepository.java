package com.example.webapp.repository;

import com.example.webapp.entity.User;
import com.example.webapp.mapper.UserMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final String SQL_GET_USER_BY_EMAIL =
            "select id, name, email, password from users where email = :email";
    private static final String SQL_GET_USER_BY_ID =
            "select id, name, email, password from users where id = :id";
    private static final String SQL_GET_USERS = "select * from users";

    private final UserMapper userMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(
            UserMapper userMapper,
            NamedParameterJdbcTemplate jdbcTemplate
    ) {
        this.userMapper = userMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getUsers() {
        List<User> users = jdbcTemplate.query(
                SQL_GET_USERS,
                this.userMapper);

        return users;
    }

    public Optional<User> getUserById(int id) {
        var params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.query(SQL_GET_USER_BY_ID, params, this.userMapper).stream().findFirst();
    }

    public Optional<User> getUserByEmail(String email) {
        var params = new MapSqlParameterSource();
        params.addValue("email", email);
        return jdbcTemplate.query(SQL_GET_USER_BY_EMAIL, params, this.userMapper).stream().findFirst();
    }
}
