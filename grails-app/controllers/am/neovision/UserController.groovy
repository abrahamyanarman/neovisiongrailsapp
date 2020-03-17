package am.neovision

import grails.plugin.springsecurity.annotation.Secured


class UserController {

    UserService userService

    def index() { }

    @Secured(value=["hasRole('ROLE_ADMIN')"], httpMethod='GET')
    def show(id){
       User user = userService.findById(id)
       [user:user]
    }
}

