package org.example.handlers;

import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.ResponseDto;
import org.example.dtos.UnregisterDeviceDto;
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
                List<String> errors = ValidatorUtil.validate(device);
                if(!errors.isEmpty()){
                    ResponseUtil.generateResponse(context, 400, new ResponseDto(false, "Validation failed when unregistering device", null, errors));
                    return;
                }
                deviceService.unregisterDevice(user.id(), device);
                ResponseUtil.generateResponse(context, 200, new ResponseDto(true, "Device successfully unregistered", null, null));
            } catch (InvalidDataException e) {
                ResponseUtil.generateResponse(context, 422, new ResponseDto(false, "Failed to unregister device", null, Collections.singletonList(e.getMessage())));
            } catch (SQLException e) {
                log.error(e.toString());
                ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Failed to unregister device", null, List.of(e.getMessage())));
            }
        });
    }
}
