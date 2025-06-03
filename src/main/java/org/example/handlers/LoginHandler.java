package org.example.handlers;

import org.example.dtos.LoginRequestDto;
import org.example.dtos.ResponseDto;
import org.example.dtos.UsersDto;
import org.example.services.UserService;
import org.example.utils.JwtUtil;
import org.example.utils.ResponseUtil;
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
                ResponseUtil.generateResponse(context, 401, new ResponseDto(false, "Invalid credentials", null, null));
            } else {
                ResponseUtil.generateResponse(context, 200, new ResponseDto(true, "Login Success", Map.of("token", JwtUtil.generateToken(user.id(), user.username(), user.role())), null));
            }
        });
    }
}
