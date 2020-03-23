import am.neovision.security.JwtTokenProvider
import am.neovision.security.MyUserDetailsService
import am.neovision.UserPasswordEncoderListener
import org.springframework.security.authentication.AuthenticationManager


// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(MyUserDetailsService)
    jwtTokenProvider(JwtTokenProvider)

}

