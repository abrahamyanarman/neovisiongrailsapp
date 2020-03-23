package am.neovision

class BootStrap {

    def init = { servletContext ->
        User user = new User()
        user.firstName = "NeoVision"
        user.lastName = "TestApi"
        user.password = "1234"
        user.username = "neovision"
        user.photoUri = "null"
        user.email = "abrahamyan.arman.94@gmail.com"
        user.save()

        Role role  = new Role()
        role.authority = "ROLE_ADMIN"
        role.save()

        role = new Role()
        role.authority = "ROLE_USER"
        role.save()

        role = new Role()
        role.authority = "ROLE_GUEST"
        role.save()


    }
    def destroy = {
    }
}
