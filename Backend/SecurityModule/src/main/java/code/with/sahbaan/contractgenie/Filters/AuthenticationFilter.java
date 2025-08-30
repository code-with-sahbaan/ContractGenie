package code.with.sahbaan.contractgenie.Filters;

import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.RequestDTO.LoginRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.LoginResponse;
import code.with.sahbaan.contractgenie.ServicesImpl.UserServiceImpl;
import code.with.sahbaan.contractgenie.Utils.FilterUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final String accessTokenExpiry;
    public final String secret;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserServiceImpl userService, String accessTokenExpiry, String secret) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.accessTokenExpiry = accessTokenExpiry;
        this.secret = secret;
        super.setFilterProcessesUrl("/user/login");

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginDTO;
        try {
            loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body", e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        Users users = userService.getUserByEmail(user.getUsername()).get();
        Algorithm algorithm = Algorithm.HMAC256((secret).getBytes());
            Date expirationTime = new Date(System.currentTimeMillis() + (Integer.parseInt(accessTokenExpiry) * 60L) * 60 * 1000);
            String accessToken = JWT.create().withSubject(user.getUsername()).withIssuer(request.getRequestURI()).withExpiresAt(expirationTime).withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);
        BaseResponse<LoginResponse> baseResponse = new BaseResponse<>();
        LoginResponse logInResponse = new LoginResponse();
        logInResponse = userService.convertEntityToDto(users, logInResponse.getClass());
        logInResponse.setAccessToken(accessToken);
        baseResponse.setResponseBody(logInResponse);
        baseResponse.setResponseMessage("Login Successfully!");
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), baseResponse);
        // If user is InActive then sending the otp email again
        if (!logInResponse.getIsActive()) {
            try {
                userService.sendOTP(users);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        FilterUtil.writeErrorResponse(response,
                "The system does not recognize email and password combination",
                HttpServletResponse.SC_UNAUTHORIZED);
    }
}
