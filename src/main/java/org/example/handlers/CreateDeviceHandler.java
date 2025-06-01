package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.CreateDeviceDto;
import org.example.dtos.ResponseDto;
import org.example.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.*;

public class CreateDeviceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(CreateDeviceHandler.class);
    private final DeviceService deviceService;

    @Inject
    public CreateDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public void handle(Context ctx) {
        AuthenticatedUserDto user = ctx.get(AuthenticatedUserDto.class);
        ctx.parse(CreateDeviceDto.class).then(device -> {
            try {
                deviceService.createDevice(device, user.id());
                ctx.getResponse().status(201).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "Device successfully created", null, null)));
            } catch (SQLException e) {
                log.error(e.toString());
                ctx.getResponse().status(500).send(new ObjectMapper().writeValueAsString(new ResponseDto(false, "Failed to create device", null, null)));
            }
        });
    }
}
