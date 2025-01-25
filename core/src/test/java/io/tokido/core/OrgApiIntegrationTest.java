package io.tokido.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.tokido.core.model.OrgDTO;
import io.tokido.core.repos.OrgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
public class OrgApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    OrgRepository orgRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        orgRepository.deleteAll();
    }

    @Test
    public void testEmptyOrgs() {
        given()
                .contentType(ContentType.JSON)
                .when()
                    .get("/api/orgs")
                .then()
                    .statusCode(200)
                    .body(".", hasSize(0));
    }

    @Test
    public void testEmptyThenAddSomeOrgs() {
        given()
                .contentType(ContentType.JSON)
                .when()
                    .get("/api/orgs")
                .then()
                    .statusCode(200)
                    .body(".", hasSize(0));

        var org1 = createOrg();
        var org2 = createOrg();

        given()
                .contentType(ContentType.JSON)
                .when()
                    .get("/api/orgs")
                .then()
                    .statusCode(200)
                    .body(".", hasSize(greaterThanOrEqualTo(2)));
    }

    public void testAddAndThenDeleteEmptyOrg() {

    }

    private OrgDTO createOrg() {
        var org = DataGenerator.newOrg();
        var id =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                            .body(org)
                            .post("/api/orgs")
                        .then()
                            .statusCode(201)
                            .body(notNullValue())
                        .extract().body().as(Long.class);
        Assert.isTrue(id != null && id > 0, "ID is null");

        return given()
                .contentType(ContentType.JSON)
                .when()
                    .pathParam("orgId", id)
                    .get("/api/orgs/{orgId}")
                .then()
                    .statusCode(200)
                    .body("id", equalTo(id.intValue()))
                    .body("orgName", equalTo(org.getOrgName()))
                .extract().body().as(OrgDTO.class);
    }

}
