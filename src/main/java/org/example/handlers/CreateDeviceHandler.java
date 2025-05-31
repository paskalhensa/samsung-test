package org.example.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.CreateDeviceDto;
import org.example.dtos.ResponseDto;
import org.example.services.DeviceService;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;

import javax.inject.Inject;

public class CreateDeviceHandler implements Handler {
    private final DeviceService deviceService;

    @Inject
    public CreateDeviceHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public void handle(Context ctx) {
        ctx.parse(CreateDeviceDto.class).then(device -> {
            deviceService.createDevice(device);
            ctx.getResponse().status(201).send(new ObjectMapper().writeValueAsString(new ResponseDto(true, "Device succesfully created", null, null)));
        });
    }
}
