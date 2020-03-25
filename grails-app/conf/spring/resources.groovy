import am.neovision.EmailCodeService
import am.neovision.EmailService
import am.neovision.security.JwtTokenProvider
import am.neovision.security.MyUserDetailsService
import am.neovision.UserPasswordEncoderListener
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(MyUserDetailsService)
    jwtTokenProvider(JwtTokenProvider)
    passwordEncoder(BCryptPasswordEncoder)
    emailService(EmailService)
    emailCodeService(EmailCodeService)
    javaMailSender(JavaMailSenderImpl)

}

