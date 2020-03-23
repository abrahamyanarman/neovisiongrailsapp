package am.neovision




class UserController {

    UserService userService

    def index() {

    }

    //@Secured(value=["hasRole('ROLE_ADMIN')"], httpMethod='GET')
    def show(){
        respond userService.whoAmI(request)
    }
}

