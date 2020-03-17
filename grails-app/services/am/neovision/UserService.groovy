package am.neovision

import grails.gorm.transactions.Transactional

@Transactional
class UserService {

    User findById(id){
        User.findById(id)
    }
}
