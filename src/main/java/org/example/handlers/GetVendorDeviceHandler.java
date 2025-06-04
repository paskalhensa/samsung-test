package org.example.handlers;

import org.example.dtos.GetVendorDeviceDto;
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

public class GetVendorDeviceHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(GetVendorDeviceHandler.class);
    private final DeviceService deviceService;

    @Inject
    public GetVendorDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public void handle(Context context) throws Exception {
        try {
            List<GetVendorDeviceDto> devices = deviceService.getVendorDevice();
            String message = devices.isEmpty() ? "No device found" : "Devices found";
            ResponseUtil.generateResponse(context, 200, new ResponseDto(true, message, devices, null));
        } catch (SQLException e) {
            log.error(e.toString());
            ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Failed to get device", null, null));
        }
    }
}
