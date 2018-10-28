package example.grails

import grails.testing.mixin.integration.Integration
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientException
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import grails.testing.spock.OnceBefore

@SuppressWarnings(['MethodName', 'DuplicateNumberLiteral', 'Instanceof'])
@Integration
class ApiPostCodeControllerSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore // <1>
    void init() {
        client  = HttpClient.create(new URL("http://localhost:$serverPort")) // <2>
    }

    def 'test /api/postCodes url is secured'() {
        when:
        HttpRequest request = HttpRequest.GET('/api/postCodes')
        client.toBlocking().exchange(request,  // <3>
                Argument.of(List, PostCodeView),
                Argument.of(CustomError))

        then:
        HttpClientException e = thrown(HttpClientException)
        e.response.status == HttpStatus.UNAUTHORIZED // <4>

        when:
        Optional<CustomError> jsonError = e.response.getBody(CustomError)

        then:
        jsonError.isPresent()
        jsonError.get().status == 401
        jsonError.get().error == 'Unauthorized'
        jsonError.get().message == 'No message available'
        jsonError.get().path == '/api/postCodes'
    }

    def "test a user with the role ROLE_ADMIN is able to access /api/postCodes url"() {
        when: 'login with the admin'
        UserCredentials credentials = new UserCredentials(username: 'admin', password: 'password')
        HttpRequest request = HttpRequest.POST('/api/login', credentials) // <5>
        HttpResponse<BearerToken> resp = client.toBlocking().exchange(request, BearerToken)

        then:
        resp.status.code == 200
        resp.body().roles.find { it == 'ROLE_ADMIN' }

        when:
        String accessToken = resp.body().accessToken

        then:
        accessToken
        println accessToken

        when:
        HttpResponse<List> rsp = client.toBlocking().exchange(HttpRequest.GET('/api/postCodes')
                .header('Authorization', "Bearer ${accessToken}"), Argument.of(List, PostCodeView)) // <6>

        then:
        rsp.status.code == 200 // <7>
        rsp.body() != null
        ((List)rsp.body()).size() == 10
        ((List)rsp.body()).get(0) instanceof PostCodeView
        ((PostCodeView) ((List)rsp.body()).get(0)).postcode == "AB10" //see postcode-outcodes.csv No.1
    }

    def "test a user with the role ROLE_USER is NOT able to access /api/postCodes url"() {
        when: 'login with the user1'

        UserCredentials creds = new UserCredentials(username: 'user1', password: 'user1')
        HttpRequest request = HttpRequest.POST('/api/login', creds)
        HttpResponse<BearerToken> resp = client.toBlocking().exchange(request, BearerToken)

        then:
        resp.status.code == 200
        !resp.body().roles.find { it == 'ROLE_ADMIN' }
        resp.body().roles.find { it == 'ROLE_USER' }

        when:
        String accessToken = resp.body().accessToken

        then:
        accessToken

        when:
        resp = client.toBlocking().exchange(HttpRequest.GET('/api/postCodes')
                .header('Authorization', "Bearer ${accessToken}"))

        then:
        def e = thrown(HttpClientException)
        e.response.status == HttpStatus.FORBIDDEN // <8>
    }
}
