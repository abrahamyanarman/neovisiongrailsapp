package am.neovision

class BootStrap {

    def init = { servletContext ->
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
