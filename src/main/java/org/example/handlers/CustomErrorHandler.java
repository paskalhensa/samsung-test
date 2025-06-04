package org.example.handlers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.dtos.ResponseDto;
import org.example.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.core.error.ServerErrorHandler;
import ratpack.core.handling.Context;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomErrorHandler implements ServerErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomErrorHandler.class);

    @Override
    public void error(Context context, Throwable throwable) throws Exception {
        if (log.isErrorEnabled()) {
            log.error("Custom error caught {}", throwable.toString());
        }
        if (throwable instanceof InvalidFormatException exception) {
            String fullPath = exception.getPath().stream().map(JsonMappingException.Reference::getFieldName).filter(Objects::nonNull).collect(Collectors.joining("."));
            String message = "Invalid format for field '%s'. Expected type: %s".formatted(fullPath, exception.getTargetType().getSimpleName());
            ResponseUtil.generateResponse(context, 400, new ResponseDto(false, "Invalid input format.", null, Collections.singletonList(message)));
        } else {
            ResponseUtil.generateResponse(context, 500, new ResponseDto(false, "Something went wrong in internal server.", null, null));
        }
    }
}
