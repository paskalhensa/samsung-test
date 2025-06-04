package org.example.handlers;

import org.example.dtos.RegisterUserDto;
import org.example.dtos.ResponseDto;
import org.example.exceptions.ClientInputException;
import org.example.services.UserService;
import org.example.utils.ResponseUtil;
import org.example.utils.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class RegisterHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(RegisterHandler.class);
    private final UserService userService;

    @Inject
    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Context context) throws Exception {
        context.parse(RegisterUserDto.class).then(user -> {
            List<String> errors = ValidatorUtil.validate(user);
            if (!errors.isEmpty()) {
                log.error("Failed to register user {} because of validation: {}", user.username(), errors);
                ResponseUtil.generateResponse(context, 400, new ResponseDto(false, "Validation failed when registering user", null, errors));
                return;
            }
            try {
                userService.createUser(user);
                log.error("{} success registered", user.username());
                ResponseUtil.generateResponse(context, 201, new ResponseDto(true, "User successfully created", null, null));
            } catch (ClientInputException e) {
                log.error("Failed to register user {} because of error: {}", user.username(), e.toString());
                ResponseUtil.generateResponse(context, 400, new ResponseDto(false, "Failed to create user", null, List.of(e.getMessage())));
            } catch (SQLException e) {
                log.error("Failed to register user {} because of error: {}", user.username(), e.toString());
                ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Failed to create user", null, null));
            }
        });
    }
}
