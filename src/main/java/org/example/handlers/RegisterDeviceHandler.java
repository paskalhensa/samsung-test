package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.RegisterDeviceDto;
import org.example.dtos.ResponseDto;
import org.example.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;

public class RegisterDeviceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(RegisterDeviceHandler.class);
    private final DeviceService deviceService;

    @Inject
    public RegisterDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    @Override
    public void handle(Context context) throws Exception {
        AuthenticatedUserDto user = context.get(AuthenticatedUserDto.class);
        context.parse(RegisterDeviceDto.class).then(device -> {
           try {
                deviceService.registerDevice(user.id(), device.deviceId());
               context.getResponse().status(201).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "Device successfully registered", null, null)));
           } catch (SQLException e) {
               log.error(e.toString());
               context.getResponse().status(500).send(new ObjectMapper().writeValueAsString(new ResponseDto(false, "Failed to register device", null, null)));
           }
        });
    }
}
