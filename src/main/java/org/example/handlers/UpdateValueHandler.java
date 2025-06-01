package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.ResponseDto;
import org.example.dtos.UpdateValueDto;
import org.example.services.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class UpdateValueHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(UpdateValueHandler.class);
    private final DeviceService deviceService;

    @Inject
    public UpdateValueHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    @Override
    public void handle(Context context) throws Exception {
        AuthenticatedUserDto user = context.get(AuthenticatedUserDto.class);
        context.parse(UpdateValueDto.class).then(device -> {
            try {
                deviceService.updateDeviceValue(user.id(), device);
                context.getResponse().status(200).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "Device successfully Updated", null, null)));
            } catch (SQLException e) {
                log.error(e.toString());
                context.getResponse().status(500).send(new ObjectMapper().writeValueAsString(new ResponseDto(false, "Failed to update device", null, List.of(e.getMessage()))));
            }
        });
    }
}
