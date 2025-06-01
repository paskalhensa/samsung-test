package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.RegisteredDeviceDto;
import org.example.dtos.ResponseDto;
import org.example.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class GetRegisteredDeviceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(GetRegisteredDeviceHandler.class);
    private final DeviceService deviceService;

    @Inject
    public GetRegisteredDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    @Override
    public void handle(Context context) throws Exception {
        AuthenticatedUserDto user = context.get(AuthenticatedUserDto.class);
        try {
            List<RegisteredDeviceDto> devices = deviceService.getRegisteredDevice(user.id());
            context.getResponse().status(200).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "Registered devices found", devices, null)));
        } catch (SQLException e) {
            log.error(e.toString());
            context.getResponse().status(500).send(new ObjectMapper().writeValueAsString(new ResponseDto(false, "Failed to get registered device", null, null)));
        }
    }
}
