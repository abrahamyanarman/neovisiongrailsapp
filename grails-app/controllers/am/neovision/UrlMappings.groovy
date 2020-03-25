package am.neovision

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "/api/auth"(controller: 'login',action: 'login')
        "/user/whoAmI"(controller: 'login',action: 'whoAmI')
        "/api/register"(controller: 'login',action: 'register')
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
