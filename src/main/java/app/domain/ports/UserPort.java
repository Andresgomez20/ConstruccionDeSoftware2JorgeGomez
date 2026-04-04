package app.domain.ports;

import java.util.List;

import app.domain.models.identity.User;

public interface UserPort {

    boolean existsByIdentificationId(String identificationId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void save(User user);
    void update(User user);
    User findByIdentificationId(String identificationId);
    User findByUsername(String username);
    List<User> findAll();

}