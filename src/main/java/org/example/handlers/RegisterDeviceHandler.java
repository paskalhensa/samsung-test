package org.example.handlers;

import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.RegisterDeviceDto;
import org.example.dtos.ResponseDto;
import org.example.exceptions.InvalidDataException;
import org.example.services.DeviceService;
import org.example.utils.ResponseUtil;
import org.example.utils.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

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
                List<String> errors = ValidatorUtil.validate(device);
                if(!errors.isEmpty()){
                    log.error("{} failed to register device {} because of validation: {}", user.id(), device.deviceId(), errors);
                    ResponseUtil.generateResponse(context, 400, new ResponseDto(false, "Validation failed when registering device", null, errors));
                    return;
                }
                deviceService.registerDevice(user.id(), device.deviceId());
                log.info("{} registered device {}", user.id(), device.deviceId());
                ResponseUtil.generateResponse(context, 201, new ResponseDto(true, "Device successfully registered", null, null));
            } catch (InvalidDataException e) {
                log.error("{} failed to register device {} with error: {}", user.id(), device.deviceId(), e.toString());
                ResponseUtil.generateResponse(context, 422, new ResponseDto(false, "Failed to register device", null, Collections.singletonList(e.getMessage())));
            } catch (SQLException e) {
                log.error("{} failed to register device {} with error: {}", user.id(), device.deviceId(), e.toString());
                ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Failed to register device", null, null));
            }
        });
    }
}
