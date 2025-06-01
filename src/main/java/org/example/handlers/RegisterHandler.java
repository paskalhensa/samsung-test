package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.RegisterUserDto;
import org.example.dtos.ResponseDto;
import org.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;

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
            try {
                userService.createUser(user);
                context.getResponse().status(201).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "User successfully created", null, null)));
            } catch (SQLException e) {
                log.error(e.toString());
                context.getResponse().status(500).send(new ObjectMapper().writeValueAsString(new ResponseDto(false, "Failed to create User", null, null)));
            }
        });
    }
}
