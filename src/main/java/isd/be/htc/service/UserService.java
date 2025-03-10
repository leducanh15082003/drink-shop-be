package isd.be.htc.service;

import isd.be.htc.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
    User updateUser(User user);
    void deleteUserById(Long id);
}
