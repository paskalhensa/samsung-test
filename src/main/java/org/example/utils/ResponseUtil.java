package org.example.utils;

import org.example.dtos.ResponseDto;
import ratpack.core.handling.Context;
import ratpack.core.jackson.Jackson;

public class ResponseUtil {
    public static void generateResponse(Context context, int status, ResponseDto responseDto) {
        context.getResponse().status(status);
        context.render(Jackson.json(responseDto));
    }
}
