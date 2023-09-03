package me.dio.sdw.service;

import me.dio.sdw.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
    User findById(Long id);
    User create(User entity);
    User update(Long id, User entity);
    void delete(Long id);
}
