package io.tokido.core;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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

        var org1 = DataGenerator.newOrg();

        var id1 =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                            .body(org1)
                            .post("/api/orgs")
                        .then()
                            .statusCode(201)
                            .body(notNullValue())
                        .extract().body().as(Long.class);

        Assert.isTrue(id1 != null && id1 > 0, "ID is null");

        given()
                .contentType(ContentType.JSON)
                .when()
                    .get("/api/orgs")
                .then()
                    .statusCode(200)
                    .body(".", hasSize(greaterThanOrEqualTo(1)));

        given()
                .contentType(ContentType.JSON)
                .when()
                    .pathParam("orgId", id1)
                    .get("/api/orgs/{orgId}")
                .then()
                    .statusCode(200)
                    .body("id", equalTo(id1.intValue()))
                    .body("orgName", equalTo(org1.getOrgName()));
    }



}
