package org.example.handlers;

import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.ResponseDto;
import org.example.dtos.UpdateDeviceDto;
import org.example.services.DeviceService;
import org.example.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class UpdateDeviceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(UpdateDeviceHandler.class);
    private final DeviceService deviceService;

    @Inject
    public UpdateDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public void handle(Context context) throws Exception {
        AuthenticatedUserDto user = context.get(AuthenticatedUserDto.class);
        context.parse(UpdateDeviceDto.class).then(device -> {
            try {
                deviceService.updateVendorDevice(user.id(), device);
                ResponseUtil.generateResponse(context, 200, new ResponseDto(true, "Device successfully Updated", null, null));
            } catch (SQLException e) {
                log.error(e.toString());
                ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Failed to update device", null, List.of(e.getMessage())));
            }
        });
    }
}
