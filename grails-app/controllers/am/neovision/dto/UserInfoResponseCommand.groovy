package am.neovision.dto

import am.neovision.Role

class UserInfoResponseCommand implements Serializable {

    private static final long serialVersionUID = 1


    String firstName
    String lastName
    String email
    String photoUri
    String username
    String token
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Collection<Role> authorities

    UserInfoResponseCommand() {
    }

    UserInfoResponseCommand(String firstName, String lastName, String email, String photoUri, String username, String token, boolean enabled, boolean accountExpired, boolean accountLocked, boolean passwordExpired, Collection<Role> authorities) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.photoUri = photoUri
        this.username = username
        this.token = token
        this.enabled = enabled
        this.accountExpired = accountExpired
        this.accountLocked = accountLocked
        this.passwordExpired = passwordExpired
        this.authorities = authorities
    }
    static constraints = {
        username nullable: false, blank: false, unique: true
        email nullable: false,blank: false, unique: true,email: true
        firstName nullable: false, blank: false
        lastName nullable: false, blank: false
        photoUri nullable: true,blank: true
    }

}
