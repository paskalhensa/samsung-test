package org.example.handlers;

import org.example.dtos.GetUserDeviceDto;
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

public class GetUserDeviceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(GetUserDeviceHandler.class);
    private final DeviceService deviceService;

    @Inject
    public GetUserDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
    @Override
    public void handle(Context context) throws Exception {
        try {
            List<GetUserDeviceDto> devices = deviceService.getUserDevice();
            String message = devices.isEmpty() ? "No user device found" : "User device count found";
            log.info("Success get user device");
            ResponseUtil.generateResponse(context, 200, new ResponseDto(true, message, devices, null));
        } catch (SQLException e) {
            log.error("Failed to get user device");
            ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Failed to get user device count", null, null));
        }
    }
}
