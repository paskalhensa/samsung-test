package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.ResponseDto;
import org.example.dtos.UserInformationDto;
import org.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class UserInformationHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(UserInformationHandler.class);
    private final UserService userService;

    @Inject
    public UserInformationHandler(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void handle(Context context) throws Exception {
        try {
            List<UserInformationDto> users = userService.getUserInformation();
            context.getResponse().status(200).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "User information found", users, null)));
        } catch (SQLException e) {
            log.error(e.toString());
            context.getResponse().status(500).send(new ObjectMapper().writeValueAsString(new ResponseDto(false, "Failed to get user information", null, null)));
        }
    }
}
