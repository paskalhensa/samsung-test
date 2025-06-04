package org.example.handlers;

import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.GetDeviceDto;
import org.example.dtos.ResponseDto;
import org.example.services.DeviceService;
import org.example.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class GetDeviceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(GetDeviceHandler.class);
    private final DeviceService deviceService;

    @Inject
    public GetDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public void handle(Context context) throws Exception {
        AuthenticatedUserDto user = context.get(AuthenticatedUserDto.class);
        try {
            List<GetDeviceDto> devices = deviceService.getDevice(user.id());
            String message = devices.isEmpty() ? "No device found" : "devices found";
            log.info("{} success get device", user.id());
            ResponseUtil.generateResponse(context, 200, new ResponseDto(true, message, devices, null));
        } catch (SQLException e) {
            log.error("{} failed to get device", user.id());
            ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Failed to get device", null, null));
        }
    }
}
