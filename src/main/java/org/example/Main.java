package org.example;

import org.example.config.DbConfig;
import org.example.handlers.CreateDeviceHandler;
import org.example.services.DeviceService;
import ratpack.core.server.RatpackServer;
import ratpack.guice.Guice;

public class Main {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(server -> server
                .registry(Guice.registry(bindings -> {
                    bindings.bindInstance(DbConfig.getValuesFromEnv());
                    bindings.bind(DeviceService.class);
                    bindings.bind(CreateDeviceHandler.class);
                }))
                .handlers(chain -> chain
                        .post("api/devices", CreateDeviceHandler.class)));
    }
}