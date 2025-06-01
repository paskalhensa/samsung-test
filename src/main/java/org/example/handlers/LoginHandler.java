package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.LoginRequestDto;
import org.example.dtos.ResponseDto;
import org.example.dtos.UsersDto;
import org.example.services.UserService;
import org.example.utils.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.util.Map;

public class LoginHandler implements Handler {
    private final UserService userService;

    @Inject
    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(Context context) throws Exception {
        context.parse(LoginRequestDto.class).then(login -> {
            UsersDto user = userService.findByUsername(login.username());
            if (user == null || !BCrypt.checkpw(login.password(), user.password())) {
                context.getResponse().status(401).send(new ObjectMapper().writeValueAsString(new ResponseDto(false, "Invalid credentials", null, null)));
            } else {
                context.getResponse().status(200).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "Login Success", Map.of("token", JwtUtil.generateToken(user.id(), user.username(), user.role())), null)));
            }
        });
    }
}
