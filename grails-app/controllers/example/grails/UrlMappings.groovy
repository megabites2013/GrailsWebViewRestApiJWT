package example.grails

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        '/api/postCodes'(controller: 'apiPostCode')

        '/'(view: '/index')
        '500'(view: '/error')
        '404'(view: '/notFound')
    }
}
