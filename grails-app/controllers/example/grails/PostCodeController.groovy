package example.grails

import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

//This is the web view's controller

@SuppressWarnings('LineLength')
@ReadOnly
class PostCodeController {

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond PostCode.list(params), model:[postCodeCount: PostCode.count()]
    }

    def show(PostCode postCode) {
        respond postCode
    }

    @SuppressWarnings(['GrailsMassAssignment', 'FactoryMethodName'])
    def create() {
        respond new PostCode(params)
    }

    @Transactional
    def save(PostCode postCode) {
        if (postCode == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (postCode.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond postCode.errors, view:'create'
            return
        }

        postCode.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'postCode.label', default: 'PostCode'), postCode.id])
                redirect postCode
            }
            '*' { respond postCode, [status: CREATED] }
        }
    }

    def edit(PostCode postCode) {
        respond postCode
    }

    @Transactional
    def update(PostCode postCode) {
        if (postCode == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (postCode.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond postCode.errors, view:'edit'
            return
        }

        postCode.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'postCode.label', default: 'PostCode'), postCode.id])
                redirect postCode
            }
            '*' { respond postCode, [status: OK] }
        }
    }

    @Transactional
    def delete(PostCode postCode) {

        if (postCode == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        postCode.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'postCode.label', default: 'PostCode'), postCode.id])
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'postCode.label', default: 'PostCode'), params.id])
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
