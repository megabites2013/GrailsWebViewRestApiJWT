package example.grails

import grails.gorm.services.Service

@Service(PostCode)
interface PostCodeService {

    PostCode get(Serializable id)

    List<PostCode> list(Map args)

    Long count()

    void delete(Serializable id)

    PostCode save(PostCode postCode)

}
