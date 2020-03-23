package am.neovision

import am.neovision.dto.UserInfoResponseCommand
import am.neovision.exception.CustomException
import am.neovision.mapper.UserMapper
import am.neovision.security.JwtTokenProvider
import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

import javax.servlet.http.HttpServletRequest


@Transactional
class UserService {

    private JwtTokenProvider jwtTokenProvider

    private AuthenticationManager authenticationManager

    UserService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider
        this.authenticationManager = authenticationManager
    }

    User findById(id){
        User.findById(id)
    }

    UserInfoResponseCommand signIn(String username, String password, boolean remember){
      Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password))
        if (authentication.isAuthenticated()){
            User user = User.findByUsername(username)
            UserInfoResponseCommand userInfoResponse = new UserMapper().apply(user)
            userInfoResponse.setToken(jwtTokenProvider.createToken(username, user.authorities as List<Role>,remember))

            return userInfoResponse
        }else {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    UserInfoResponseCommand whoAmI(HttpServletRequest httpServletRequest) {
        String userName = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(httpServletRequest))
        if (!userName.isEmpty()){
            return new UserMapper().apply(User.findByUsername(userName))
        }else {
            throw new CustomException("Please signIn!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
