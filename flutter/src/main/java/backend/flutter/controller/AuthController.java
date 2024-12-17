package backend.flutter.controller;


import backend.flutter.dto.request.RequestLogin;

import backend.flutter.dto.response.ResponseLogin;
import backend.flutter.entity.User;
import backend.flutter.exception.IdInvalidException;
import backend.flutter.service.ModelService;
import backend.flutter.service.UserService;
import backend.flutter.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${lieu.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;


    private ModelService modelService;
    private UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, ModelService modelService, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.modelService = modelService;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseLogin> login(@Valid @RequestBody RequestLogin requestLogin) throws MethodArgumentNotValidException {
        // nap input email va pasword vao
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                requestLogin.getUsername(), requestLogin.getPassword()
        );
        // xac thuc nguoi dung thong qua ham loadUserbyUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // xét thông tin người dùng đăng nhập vào context ( có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResponseLogin res = new ResponseLogin();
        User currentDB = this.userService.getUserByEmail(requestLogin.getUsername());
        if (currentDB != null) {
            ResponseLogin.UserLogin userLogin = new ResponseLogin.UserLogin(
                    currentDB.getId(),
                    currentDB.getEmail(),
                    currentDB.getUsername());
            res.setUser(userLogin);

        }
        String access_token = this.modelService.createAccessToken(authentication.getName(), res.getUser());
        res.setAccessToken(access_token);
        String refersh_token = this.modelService.createRefreshToken(requestLogin.getUsername(), res);
        this.userService.updateUserToken(refersh_token, requestLogin.getUsername());
        ResponseCookie resCookie = ResponseCookie
                .from("refresh_token", refersh_token)
                .httpOnly(true)
                // secure chỉ dùng cho https, http không tạo dược, dùng postman thì đc all
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResponseLogin.UserGetAccount> getAccount() {
        String email = ModelService.getCurrentUserLogin().isPresent() ? ModelService.getCurrentUserLogin().get() : "";
        User currentUserDB = this.userService.getUserByEmail(email);
        ResponseLogin.UserLogin userLogin = new ResponseLogin.UserLogin();
        ResponseLogin.UserGetAccount userGetAccount = new ResponseLogin.UserGetAccount();
        if (currentUserDB != null) {
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setUsername(currentUserDB.getUsername());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get user by refresh token")
    public ResponseEntity<ResponseLogin> getRefreshToken( @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws IdInvalidException {
        if (refresh_token.equals("abc")) {
            throw new IdInvalidException("không có refresh token ở cookie");
        }
        //check valid
        Jwt decodedToken = this.modelService.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject();
        //check user by token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentUser == null) {
            throw new IdInvalidException("Refresh Token khoong hop le");
        }

        ResponseLogin res = new ResponseLogin();
        User currentDB = this.userService.getUserByEmail(email);
        if (currentDB != null) {
            ResponseLogin.UserLogin userLogin = new ResponseLogin.UserLogin(
                    currentDB.getId(),
                    currentDB.getEmail(),
                    currentDB.getUsername());
            res.setUser(userLogin);

        }
        String access_token = this.modelService.createAccessToken(email, res.getUser());
        res.setAccessToken(access_token);
        String new_refersh_token = this.modelService.createRefreshToken(email, res);
        // update user
        this.userService.updateUserToken(new_refersh_token, email);
        ResponseCookie resCookie = ResponseCookie
                .from("refresh_token", new_refersh_token)
                .httpOnly(true)
                // secure chỉ dùng cho https, http không tạo dược, dùng postman thì đc all
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);
    }
    @PostMapping("auth/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = ModelService.getCurrentUserLogin().isPresent() ? ModelService.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Access token khong hop le");
        }
        // update refresh token = null
        this.userService.updateUserToken(null, email);
        // remote refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token",null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,deleteSpringCookie.toString())
                .body(null);
    }
}
