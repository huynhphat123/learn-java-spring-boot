package com.phatdev.controller;

import com.nimbusds.jose.JOSEException;
import com.phatdev.dto.request.ApiReponse;
import com.phatdev.dto.request.AuthenticationRequest;
import com.phatdev.dto.request.IntrospectRequest;
import com.phatdev.dto.response.AuthenticationResponse;
import com.phatdev.dto.response.IntrospectResponse;
import com.phatdev.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiReponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
            return ApiReponse.<AuthenticationResponse>builder()
                        .result(result)
                    .build();
    }

    @PostMapping("/introspect")
    ApiReponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request); // Gọi phương thức introspect từ service
        return ApiReponse.<IntrospectResponse>builder()
                .result(result) // Trả về kết quả
                .build();
    }


}
