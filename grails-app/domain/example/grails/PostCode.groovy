package example.grails

import groovy.transform.CompileStatic
import com.opencsv.bean.CsvBindByName


class PostCode {

    Long id

    @CsvBindByName
    String postcode

    @CsvBindByName
    double latitude

    @CsvBindByName
    double longitude

    static constraints = {
        postcode nullable: false
    }
}
