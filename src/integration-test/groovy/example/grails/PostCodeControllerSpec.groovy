package example.grails

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SuppressWarnings('MethodName')
@Integration
class PostCodeControllerSpec extends GebSpec {

    void 'test /postCode/index is secured, but accesible to users with role ROLE_ADMIN'() {
        when: 'try to visit postCode listing without login'
        go '/postCode/index'

        then: 'it is redirected to login page'
        at LoginPage

        when: 'signs in with a ROLE_ADMIN user'
        LoginPage page = browser.page(LoginPage)
        page.login('admin', 'password')

        then: 'he gets access to the postCode listing page'
        at PostCodeListingPage
    }

    void 'test /postCode/index is secured, but accesible to users with role ROLE_USER'() {
        when: 'try to visit postCode listing without login'
        go '/postCode/index'

        then: 'it is redirected to login page'
        at LoginPage

        when: 'signs in with a ROLE_USER user'
        LoginPage page = browser.page(LoginPage)
        page.login('user1', 'user1')

        then: 'he gets access to the postCode listing page'
        at PostCodeListingPage
    }
}
