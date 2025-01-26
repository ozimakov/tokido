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

    @Test
    public void testAddAndThenDeleteOrg() {
        var org = createOrg();

        Assert.isTrue(orgRepository.count() == 1, "Unexpected number of orgs in the repo: not 1");

        given()
            .contentType(ContentType.JSON)
            .when()
                .pathParam("orgId", org.getId())
                .delete("/api/orgs/{orgId}")
            .then()
                .statusCode(204);

        Assert.isTrue(orgRepository.count() == 0, "Unexpected number of orgs in the repo: not 0");
    }

    @Test
    public void testUpdate() {
        var org = createOrg();

        org.setOrgName(org.getOrgName() + "_UPD");

        given()
            .contentType(ContentType.JSON)
            .when()
                .pathParam("orgId", org.getId())
                .body(org)
                .put("/api/orgs/{orgId}")
            .then()
                .statusCode(200)
                .body(".", equalTo(org.getId().intValue()));

        given()
            .contentType(ContentType.JSON)
            .when()
                .pathParam("orgId", org.getId())
                .get("/api/orgs/{orgId}")
            .then()
                .statusCode(200)
                .body("id", equalTo(org.getId().intValue()))
                .body("orgName", equalTo(org.getOrgName()));
    }

    @Test
    public void testCreateWithOwnId() {
        var org = DataGenerator.newOrg();
        org.setId(999L);

        given()
            .contentType(ContentType.JSON)
            .when()
                .body(org)
                .post("/api/orgs")
            .then()
                .statusCode(201)
                .body(".", not(equalTo(org.getId().intValue())));
    }

    @Test
    public void testNotFound() {
        var org1 = createOrg();
        var org2 = createOrg();
        var org3 = createOrg();

        testFetchNotFound();
        testDeleteNotFound();
    }

    private void testFetchNotFound() {
        given()
            .contentType(ContentType.JSON)
            .when()
                .pathParam("orgId", 1)  // must never exist as mongo set to start counters with 10000
                .get("/api/orgs/{orgId}")
            .then()
                .statusCode(404);
    }

    private void testDeleteNotFound() {
        var id = 1;
        given()
            .contentType(ContentType.JSON)
            .when()
                .pathParam("orgId", 1)  // must never exist as mongo set to start counters with 10000
                .delete("/api/orgs/{orgId}")
            .then()
                .statusCode(404);
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
