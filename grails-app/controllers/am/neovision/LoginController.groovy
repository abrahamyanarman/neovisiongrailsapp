package am.neovision

import am.neovision.dto.ErrorResponseCommand
import am.neovision.dto.SignInRequestCommand
import am.neovision.dto.UserInfoResponseCommand
import am.neovision.mapper.UserMapper
import am.neovision.security.JwtTokenUtil
import am.neovision.security.MyUserDetailsService
import grails.converters.JSON
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.lang.NonNull
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.RequestBody


class LoginController {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private MyUserDetailsService userDetailsService;

    LoginController(JwtTokenUtil jwtTokenUtil,
                           MyUserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }


    def  login(@RequestBody @NonNull SignInRequestCommand request) {
        User userDetails
        UserInfoResponseCommand userResponse;

        try {
            authenticate(request.username, request.password);

            userDetails =  userDetailsService
                    .loadUserByUsername(request.username)
        } catch (Exception e) {
            ErrorResponseCommand response = new ErrorResponseCommand(e.getMessage());
            render( new ResponseEntity<>(response, HttpStatus.BAD_REQUEST))
        }
        String token = jwtTokenUtil.generateToken(userDetails);
        UserMapper userMapper = new UserMapper();
        userResponse = userMapper.apply(userDetails);
        userResponse.setToken(token);

         render(userResponse, HttpStatus.OK) as JSON//TODO complate
    }


    private void authenticate(String userEmail, String password) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
        } catch (DisabledException e) {
            throw new Exception("User Disabled" +
                    "", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Bad Credentials", e);
        }
    }
}
