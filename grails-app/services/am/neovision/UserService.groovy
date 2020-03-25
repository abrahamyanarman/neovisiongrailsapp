package am.neovision

import am.neovision.dto.SignUpRequestCommand
import am.neovision.dto.UserInfoResponseCommand
import am.neovision.exception.CustomException
import am.neovision.mapper.UserMapper
import am.neovision.security.JwtTokenProvider
import grails.gorm.transactions.Transactional
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder

import javax.servlet.http.HttpServletRequest


@Transactional
class UserService {

    private JwtTokenProvider jwtTokenProvider
    private AuthenticationManager authenticationManager
    private PasswordEncoder passwordEncoder
    private EmailService emailService


    UserService(JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider
        this.authenticationManager = authenticationManager
        this.emailService = emailService
        this.passwordEncoder = passwordEncoder
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

    User findByEmail(String email) {
        return User.findByEmail(email)
    }

    void enableUser(String email) {
        User user = User.findByEmail(email)
        user.enabled = true
        user.save()
    }

    void changePassword(String email, String password) {
        User user = User.findByEmail(email)
        user.password = passwordEncoder.encode(password)
        user.save()
    }

    void createNewUser(SignUpRequestCommand signUpRequestCommand) {
        User user = new User()
        user.firstName = signUpRequestCommand.firstName
        user.lastName = signUpRequestCommand.lastName
        user.email = signUpRequestCommand.email
        user.username = signUpRequestCommand.username
        user.photoUri = signUpRequestCommand.photoUri
        user.password = passwordEncoder.encode(signUpRequestCommand.password)
        user.enabled = false
        user.accountExpired = false
        user.accountLocked = false
        user.passwordExpired = false
        user.withNewSession {user.save()}
        List<Role> roles = new ArrayList<>()
        signUpRequestCommand.roles.each {
            role->
                roles.add(Role.findByAuthority(role))
        }

        roles.each{ role ->
            UserRole.withNewSession {
                UserRole.create(User.findByEmail(user.email),role)
            }

        }

        emailService.changePasswordForFirstLogin(User.findByEmail(signUpRequestCommand.email))
    }
}
