package am.neovision

import am.neovision.dto.SignInRequestCommand
import am.neovision.dto.SignUpRequestCommand
import grails.gorm.transactions.Transactional
import org.springframework.lang.NonNull
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RequestBody


class LoginController {

    static allowedMethods = [
            register: "POST",
            login: "POST",
            whoAmI: "GET"
    ]
    static responseFormats = ['json']

    private UserService userService

    LoginController(UserService userService) {
        this.userService = userService
    }

    def login(@RequestBody @NonNull SignInRequestCommand request) {
        respond userService.signIn(request.username, request.password, request.rememberMe)

    }

    def whoAmI() {
        respond userService.whoAmI(request)
    }

    def register(@RequestBody @NonNull SignUpRequestCommand signUpRequestCommand){
        userService.createNewUser(signUpRequestCommand)
        render "Check your email to activate your account"

    }

}
