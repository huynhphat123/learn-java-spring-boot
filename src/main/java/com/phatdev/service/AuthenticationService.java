package com.phatdev.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.phatdev.dto.request.AuthenticationRequest;
import com.phatdev.dto.request.IntrospectRequest;
import com.phatdev.dto.response.AuthenticationResponse;
import com.phatdev.dto.response.IntrospectResponse;
import com.phatdev.expection.AppException;
import com.phatdev.expection.ErrorCode;
import com.phatdev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    @NonFinal
    // Inject SINGER_KEY từ application.properties
    @Value("${jwt.singer-key}")
    protected String SINGER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken(); // Lấy token từ yêu cầu

        JWSVerifier verifier = new MACVerifier(SINGER_KEY.getBytes()); // Tạo một verifier sử dụng SINGER_KEY để xác minh token

        SignedJWT signedJWT = SignedJWT.parse(token); // Phân tích token thành đối tượng SignedJWT

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime(); // Lấy thời gian hết hạn từ token

        var verified = signedJWT.verify(verifier); // Xác minh token với verifier

       return IntrospectResponse.builder()
               .valid(verified && expityTime.after(new Date()))  // Kiểm tra xem token có hợp lệ và chưa hết hạn
               .build();

    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()) // Tìm user trong DB
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED)); // Nếu không tồn tại thì ném lỗi

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10); // Mã hóa mật khẩu
        boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword()); // Kiểm tra mật khẩu
    if(!authenticated)
        throw new AppException(ErrorCode.AUTHENTICATED); // Nếu sai, ném lỗi

    var token = generateToken(request.getUsername()); // Nếu đúng, tạo JWT token

    return AuthenticationResponse.builder()
            .token(token) // Trả về token cho người dùng
            .authenticated(true) // Xác nhận là người dùng đã xác thực thành công
            .build();
    }
    private String generateToken(String username) {
        // Tạo Header: Sử dụng thuật toán HS512
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username) // // Đặt username trong subject
                .issuer("phatdev") // Đặt nhà phát hành (issuer) là phatdev
                .issueTime(new Date())  // Thời gian phát hành token là hiện tại
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() // Token sẽ hết hạn sau 1 giờ
                ))
                .claim("customClaims","custom") // Thông tin tùy chỉnh
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // Chuyển payload thành JSON

        JWSObject jwsObject = new JWSObject(header, payload); // Tạo đối tượng JWT từ header và payload

        try {
            jwsObject.sign(new MACSigner(SINGER_KEY.getBytes())); // Ký token bằng SINGER_KEY (chuỗi bí mật)
            return jwsObject.serialize(); // Chuyển token thành chuỗi
        } catch (JOSEException e) {
            log.error("Cannot create token", e); // Nếu có lỗi khi ký token, ghi log và ném ngoại lệ
            throw new RuntimeException(e);
        }
    }
}
