package am.neovision

import grails.plugin.springsecurity.annotation.Secured


class UserController {

    UserService userService

    def index() {
        respond(authenticatedUser)
    }

    @Secured(value=["hasRole('ROLE_ADMIN')"], httpMethod='GET')
    def show(){
        respond(authenticatedUser)
    }
}

