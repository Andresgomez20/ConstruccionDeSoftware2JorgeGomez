package app.application.usecases;

import app.domain.Exceptions.BusinessException;
import app.domain.models.identity.User;
import app.domain.services.CreateUser;
import org.springframework.stereotype.Service;


@Service
public class UserUseCase {

    private final CreateUser createUser;
    // Aquí podrías inyectar otros servicios como FindUser, UpdateUser, etc.

    public UserUseCase(CreateUser createUser) {
        this.createUser = createUser;
    }

    public void registerUser(User user) throws BusinessException {
        createUser.execute(user);
    }
}