package example.grails

import grails.rest.RestfulController
import groovy.transform.CompileStatic

//this is the rest api controller

@CompileStatic
class ApiPostCodeController extends RestfulController {
    static responseFormats = ['json']

    ApiPostCodeController() {
        super(PostCode)
    }
}
