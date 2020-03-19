package am.neovision

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import org.springframework.security.core.userdetails.UserDetails

import javax.transaction.Transactional

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
@Transactional
class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1


    String firstName
    String lastName
    String email
    String photoUri
    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Collection<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as Collection<UserRole>)*.role as Collection<Role>
    }

    @Override
    boolean isAccountNonExpired() {
        return !accountExpired
    }

    @Override
    boolean isAccountNonLocked() {
        return !accountLocked
    }

    @Override
    boolean isCredentialsNonExpired() {
        return !passwordExpired
    }
    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        email nullable: false,blank: false, unique: true,email: true
        firstName nullable: false, blank: false
        lastName nullable: false, blank: false
        photoUri nullable: true,blank: true
    }



    static transients = ['springSecurityService']

    static mapping = {
	    password column: '`password`'
        firstName column: '`first_name`'
        lastName column: '`last_name`'
        photoUri column: '`photo_uri`'

    }
}
