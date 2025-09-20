package com.sope.sope_ecommerce_backend.controllers;


import io.livekit.server.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/livekit")
@CrossOrigin(origins = "${app.frontend.url:http://localhost:3000}", methods = { RequestMethod.GET })
public class LiveKitController {

    @Value("${livekit.api.key:devkey}")
    private String apiKey;

    @Value("${livekit.api.secret:secret}")
    private String apiSecret;

    @GetMapping("/token")
    public String createToken(
            @RequestParam @NotBlank String room,
            @RequestParam @NotBlank String identity,
            @RequestParam(defaultValue = "false") boolean isPublisher) {

        if (room.length() > 50 || identity.length() > 50) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room or identity too long");
        }
        if (!room.matches("^[a-zA-Z0-9_-]+$") || !identity.matches("^[a-zA-Z0-9_-]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid room or identity format");
        }

        try {
            // Create token
            AccessToken token = new AccessToken(apiKey, apiSecret);
            token.setIdentity(identity);
            token.addGrants(new RoomJoin(true), new RoomName(room), new CanPublish(isPublisher), new CanSubscribe(!isPublisher), new CanPublishData(true));

            return token.toJwt();

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate token", e);
        }
    }
}


// /api/livekit/token?room=shop1&identity=user123&isPublisher=true.