package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.ResponseDto;
import org.example.dtos.UnregisterDeviceDto;
import org.example.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class UnregisterDeviceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(UnregisterDeviceHandler.class);
    private final DeviceService deviceService;

    @Inject
    public UnregisterDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    @Override
    public void handle(Context context) throws Exception {
        AuthenticatedUserDto user = context.get(AuthenticatedUserDto.class);
        context.parse(UnregisterDeviceDto.class).then(device -> {
            try {
                deviceService.unregisterDevice(user.id(), device);
                context.getResponse().status(200).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "Device successfully unregistered", null, null)));
            } catch (SQLException e) {
                log.error(e.toString());
                context.getResponse().status(500).send(new ObjectMapper().writeValueAsString(new ResponseDto(false, "Failed to unregister device", null, List.of(e.getMessage()))));
            }
        });
    }
}
