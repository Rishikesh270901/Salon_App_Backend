package com.Rishikesh.UserService.service;

import com.Rishikesh.UserService.exception.UserException;
import com.Rishikesh.UserService.modal.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long Id) throws UserException;
    List<User> getAllUsers();
    void deleteUser(Long Id) throws UserException;
    User updateuser(Long id, User user) throws UserException;
}
