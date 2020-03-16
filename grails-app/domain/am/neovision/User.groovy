package am.neovision

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

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

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
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
