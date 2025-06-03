package org.example.handlers;

import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.CreateDeviceDto;
import org.example.dtos.ResponseDto;
import org.example.exceptions.ClientInputException;
import org.example.services.DeviceService;
import org.example.utils.ResponseUtil;
import org.example.utils.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.*;
import java.util.List;

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
            List<String> errors = ValidatorUtil.validate(device);
            if(!errors.isEmpty()){
                ResponseUtil.generateResponse(ctx, 400, new ResponseDto(true, "Validation failed", null, errors));
                return;
            }
            try {
                deviceService.createDevice(device, user.id());
                ResponseUtil.generateResponse(ctx, 201, new ResponseDto(true, "Device successfully created", null, null));
            } catch (ClientInputException e) {
                log.error(e.toString());
                ResponseUtil.generateResponse(ctx, 400, new ResponseDto(true, "Failed to create device", null, List.of(e.getMessage())));
            } catch (SQLException e) {
                log.error(e.toString());
                ResponseUtil.generateResponse(ctx, 500, new ResponseDto(false, "Failed to create device", null, null));
            }
        });
    }
}
