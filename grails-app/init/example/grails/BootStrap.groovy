package example.grails

import grails.compiler.GrailsCompileStatic
import com.opencsv.bean.CsvToBeanBuilder


class BootStrap {
    def FILE_POSTCODE_OUTCODES_CSV = 'postcode-outcodes.csv'

    def init = { servletContext ->
        if (PostCode.count == 0) {
            createAdminUser()
            PostCode.saveAll(loadPostCodeData())
        }
    }
    def destroy = {
    }

    /**
     * Load some demo UK postcode from reading a boundle csv file.
     *
     * @return the List<PostCode> later import to db
     */
    private List<PostCode> loadPostCodeData() {
        InputStream ins = BootStrap.class.getClassLoader().getResourceAsStream(FILE_POSTCODE_OUTCODES_CSV);
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
        List<PostCode> postCodeList = new ArrayList<>();
        try {
            postCodeList = new CsvToBeanBuilder(reader).withType(PostCode.class).build().parse();
            log.debug("processing number of  entries in CSV file:" + postCodeList.size());
        } catch (Exception e) {
            log.error("processing csv file error:" + e.getMessage());
        }
        return postCodeList;
    }

    private createAdminUser() {
        User user = User.create()
        user.username = "admin"
        user.password = "password"
        user.email = "a@b.co"
        user.salary = 3000
        user.save()

        User user2 = User.create()
        user2.username = "user1"
        user2.password = "user1"
        user2.email = "ua@b.co"
        user2.salary = 2000
        user2.save()

        SecurityRole role = SecurityRole.create()
        role.authority = "ROLE_ADMIN"
        role.save()

        SecurityRole role2 = SecurityRole.create()
        role2.authority = "ROLE_USER"
        role2.save()

        UserSecurityRole.create user, role, true
        UserSecurityRole.create user2, role2, true

    }
}

