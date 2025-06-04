package org.example.handlers;

import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.ResponseDto;
import org.example.dtos.UpdateValueDto;
import org.example.exceptions.ClientInputException;
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
                List<String> errors = ValidatorUtil.validate(device);
                if(!errors.isEmpty()){
                    log.error("{} failed to update user device {} because of validation: {}", user.id(), device.userDeviceId(), errors);
                    ResponseUtil.generateResponse(context, 400, new ResponseDto(false, "Validation failed when updating device value", null, errors));
                    return;
                }
                deviceService.updateDeviceValue(user.id(), device);
                log.info("{} updated user device {}", user.id(), device.userDeviceId());
                ResponseUtil.generateResponse(context, 200, new ResponseDto(true, "Device value successfully updated", null, null));
            } catch (ClientInputException e) {
                log.error("{} failed to update user device {} with error: {}", user.id(), device.userDeviceId(), e.toString());
                ResponseUtil.generateResponse(context, 400, new ResponseDto(false, "Failed to update device value", null, Collections.singletonList(e.getMessage())));
            } catch (InvalidDataException e) {
                log.error("{} failed to update user device {} with error: {}", user.id(), device.userDeviceId(), e.toString());
                ResponseUtil.generateResponse(context, 422, new ResponseDto(false, "Failed to update device value", null, Collections.singletonList(e.getMessage())));
            } catch (SQLException e) {
                log.error("{} failed to update user device {} with error: {}", user.id(), device.userDeviceId(), e.toString());
                ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Failed to update device value", null, Collections.singletonList(e.getMessage())));
            }
        });
    }
}
