package org.example.handlers;

import io.jsonwebtoken.ExpiredJwtException;
import org.example.dtos.AuthenticatedUserDto;
import org.example.dtos.ResponseDto;
import org.example.utils.JwtUtil;
import org.example.utils.ResponseUtil;
import ratpack.core.handling.Context;
import ratpack.core.handling.Handler;
import ratpack.exec.registry.Registry;

public class AuthHandler implements Handler {
    private final String requiredRole;

    public AuthHandler(String requiredRole) {
        this.requiredRole = requiredRole;
    }

    @Override
    public void handle(Context context) throws Exception {
        String authHeader = context.getRequest().getHeaders().get("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ResponseUtil.generateResponse(context, 401, new ResponseDto(false, "Missing or invalid Authorization header", null, null));
            return;
        }
        String token = authHeader.substring("Bearer ".length());
        try {
            AuthenticatedUserDto user = JwtUtil.decodeToken(token);
            if (!requiredRole.equals(user.role())) {
                ResponseUtil.generateResponse(context, 403, new ResponseDto(false, "Forbidden", null, null));
                return;
            }
            context.next(Registry.single(user));
        } catch (ExpiredJwtException e) {
            ResponseUtil.generateResponse(context, 401, new ResponseDto(false, "Expired token, please re-login.", null, null));
        }
    }
}
