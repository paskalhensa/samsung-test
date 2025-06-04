package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.handlers.*;
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
                    bindings.bindInstance(ObjectMapper.class, new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
                    bindings.bind(CustomErrorHandler.class);
                    bindings.bind(DeviceService.class);
                    bindings.bind(UserService.class);
                    bindings.bind(CreateDeviceHandler.class);
                    bindings.bind(LoginHandler.class);
                    bindings.bind(GetDeviceHandler.class);
                    bindings.bind(RegisterHandler.class);
                    bindings.bind(GetAvailableDeviceHandler.class);
                    bindings.bind(RegisterDeviceHandler.class);
                    bindings.bind(GetRegisteredDeviceHandler.class);
                    bindings.bind(UpdateValueHandler.class);
                    bindings.bind(UnregisterDeviceHandler.class);
                    bindings.bind(UpdateDeviceHandler.class);
                    bindings.bind(DeleteDeviceHandler.class);
                    bindings.bind(GetVendorDeviceHandler.class);
                    bindings.bind(GetUserDeviceHandler.class);
                    bindings.bind(UserInformationHandler.class);
                    bindings.bind(GetDeviceInformationHandler.class);
                }))
                .handlers(chain -> chain
                        .post("login", LoginHandler.class)
                        .post("register", RegisterHandler.class)
                        .prefix("api", api -> api
                                .prefix("vendor", vendor -> vendor
                                        .all(new AuthHandler("vendor"))
                                        .get("device-information", GetDeviceInformationHandler.class)
                                        .path("devices", devices -> devices
                                                .byMethod(method -> method
                                                        .get(GetDeviceHandler.class)
                                                        .post(CreateDeviceHandler.class)
                                                        .put(UpdateDeviceHandler.class)
                                                        .delete(DeleteDeviceHandler.class)
                                                )
                                        )
                                )
                                .prefix("users", user -> user
                                        .all(new AuthHandler("client"))
                                        .get("available-devices", GetAvailableDeviceHandler.class)
                                        .path("devices", devices -> devices
                                                .byMethod(method -> method
                                                        .post(RegisterDeviceHandler.class)
                                                        .get(GetRegisteredDeviceHandler.class)
                                                        .patch(UpdateValueHandler.class)
                                                        .delete(UnregisterDeviceHandler.class)
                                                )
                                        )
                                )
                                .prefix("admin", admin -> admin
                                        .all(new AuthHandler("admin"))
                                        .get("devices", GetVendorDeviceHandler.class)
                                        .get("users", GetUserDeviceHandler.class)
                                        .get("user-information", UserInformationHandler.class)
                                )
                        )
                )
        );
    }
}