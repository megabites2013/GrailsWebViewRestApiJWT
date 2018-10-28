grails {
    plugin {
        springsecurity {
            rest {
                token {
                    storage {
                        jwt {
                            secret = 'pleaseChangeThisSecretForANewOne'
                        }
                    }
                }
            }
            securityConfigType = "InterceptUrlMap"  // <1>
            filterChain {
                chainMap = [
                [pattern: '/api/**',filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'],// <2>
                [pattern: '/**', filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'] // <3>
                ]
            }
            userLookup {
                userDomainClassName = 'example.grails.User' // <4>
                authorityJoinClassName = 'example.grails.UserSecurityRole' // <4>
            }
            authority {
                className = 'example.grails.SecurityRole' // <4>
            }
            interceptUrlMap = [
                    [pattern: '/',                      access: ['permitAll']],
                    [pattern: '/error',                 access: ['permitAll']],
                    [pattern: '/index',                 access: ['permitAll']],
                    [pattern: '/index.gsp',             access: ['permitAll']],
                    [pattern: '/shutdown',              access: ['permitAll']],
                    [pattern: '/assets/**',             access: ['permitAll']],
                    [pattern: '/**/js/**',              access: ['permitAll']],
                    [pattern: '/**/css/**',             access: ['permitAll']],
                    [pattern: '/**/images/**',          access: ['permitAll']],
                    [pattern: '/**/favicon.ico',        access: ['permitAll']],
                    [pattern: '/login/**',              access: ['permitAll']], // <5>
                    [pattern: '/logout',                access: ['permitAll']],
                    [pattern: '/logout/**',             access: ['permitAll']],            
                    [pattern: '/postCode',          access: ['ROLE_ADMIN', 'ROLE_USER']],
                    [pattern: '/postCode/index',    access: ['ROLE_ADMIN', 'ROLE_USER']],  // <6>
                    [pattern: '/postCode/create',   access: ['ROLE_ADMIN']],
                    [pattern: '/postCode/save',     access: ['ROLE_ADMIN']],
                    [pattern: '/postCode/update',   access: ['ROLE_ADMIN']],
                    [pattern: '/postCode/delete/*', access: ['ROLE_ADMIN']],
                    [pattern: '/postCode/edit/*',   access: ['ROLE_ADMIN']],
                    [pattern: '/postCode/show/*',   access: ['ROLE_ADMIN', 'ROLE_USER']],
                    [pattern: '/api/login',             access: ['ROLE_ANONYMOUS']], // <7>
                    [pattern: '/oauth/access_token',    access: ['ROLE_ANONYMOUS']], // <8>
                    [pattern: '/api/postCodes',     access: ['ROLE_ADMIN'], httpMethod: 'GET'],  // <9>
                    [pattern: '/api/postCodes/*',   access: ['ROLE_ADMIN'], httpMethod: 'GET'],
                    [pattern: '/api/postCodes/*',   access: ['ROLE_ADMIN'], httpMethod: 'DELETE'],
                    [pattern: '/api/postCodes',     access: ['ROLE_ADMIN'], httpMethod: 'POST'],
                    [pattern: '/api/postCodes/*',   access: ['ROLE_ADMIN'], httpMethod: 'PUT']
            ]
        }
    }
}

