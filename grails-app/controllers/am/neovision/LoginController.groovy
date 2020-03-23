package am.neovision

import am.neovision.dto.SignInRequestCommand
import org.springframework.lang.NonNull
import org.springframework.web.bind.annotation.RequestBody

import javax.servlet.http.HttpServletRequest


class LoginController {

    static responseFormats = ['json']

    private UserService userService

    LoginController(UserService userService) {
        this.userService = userService
    }

    def login(@RequestBody @NonNull SignInRequestCommand request) {
        respond userService.signIn(request.username, request.password, request.rememberMe)

    }

    def whoAmI() {
        respond userService.whoAmI(request);
    }



}
