package example.grails

import geb.Page

class PostCodeListingPage extends Page {
    static url = '/postCode/index'

    static at = {
        $('#list-postCode').text()?.contains 'PostCode List'
    }
}
