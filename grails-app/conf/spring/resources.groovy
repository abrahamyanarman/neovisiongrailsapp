import am.neovision.security.MyUserDetailsService
import am.neovision.UserPasswordEncoderListener
import am.neovision.security.JwtAuthenticationEntryPoint
import am.neovision.security.JwtRequestFilter
import am.neovision.security.JwtTokenUtil

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(MyUserDetailsService)
    jwtAuthenticationEntryPoint(JwtAuthenticationEntryPoint)
    jwtRequestFilter(JwtRequestFilter)
    jwtToken(JwtTokenUtil)
}

