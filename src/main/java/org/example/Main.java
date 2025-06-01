package org.example;

import org.example.handlers.AuthHandler;
import org.example.handlers.CreateDeviceHandler;
import org.example.handlers.LoginHandler;
import org.example.services.DeviceService;
import org.example.services.UserService;
import org.example.utils.DataSourceProvider;
import ratpack.core.server.RatpackServer;
import ratpack.guice.Guice;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(server -> server
                .registry(Guice.registry(bindings -> {
                    bindings.bindInstance(DataSource.class, DataSourceProvider.getDataSource());
                    bindings.bind(DeviceService.class);
                    bindings.bind(UserService.class);
                    bindings.bind(CreateDeviceHandler.class);
                    bindings.bind(LoginHandler.class);
                }))
                .handlers(chain -> chain
                        .post("login", LoginHandler.class)
                        .prefix("api", api -> api
                                .prefix("vendor", vendor -> vendor
                                        .all(new AuthHandler("vendor"))
                                        .post("devices", CreateDeviceHandler.class)
                                )
                        )
                )
        );
    }
}