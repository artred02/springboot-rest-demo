package org.grostarin.springboot.demorest.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.grostarin.springboot.demorest.domain.DeniedBook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpringBootBootstrapLiveForDeniedTest {

    @LocalServerPort
    private int port;
       
    private String getApiRoot() {
        return "http://localhost:"+port+"/api/denied/books";
    }

    @Test
    public void whenGetAllDeniedBooks_thenOK() {
        final Response response = RestAssured.get(getApiRoot());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetDeniedBooksByTitle_thenOK() {
        final DeniedBook deniedBook = createRandomDeniedBook();
        createDeniedBookAsUri(deniedBook);

        final Response response = RestAssured.get(getApiRoot() + "?title=" + deniedBook.getTitle());
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class)
            .size() > 0);
    }

    @Test
    public void whenGetCreatedDeniedBookById_thenOK() {
        final DeniedBook deniedBook = createRandomDeniedBook();
        final String location = createDeniedBookAsUri(deniedBook);

        final Response response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(deniedBook.getTitle(), response.jsonPath()
            .get("title"));
    }

    @Test
    public void whenGetNotExistDeniedBookById_thenNotFound() {
        final Response response = RestAssured.get(getApiRoot() + "/" + randomNumeric(4));
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    // POST
    @Test
    public void whenCreateNewDeniedBook_thenCreated() {
        final DeniedBook deniedBook = createRandomDeniedBook();

        final Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(deniedBook)
            .post(getApiRoot());
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    public void whenInvalidDeniedBook_thenError() {
        final DeniedBook deniedBook = createRandomDeniedBook();
        deniedBook.setAuthor(null);

        final Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(deniedBook)
            .post(getApiRoot());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedDeniedBook_thenUpdated() {
        final DeniedBook deniedBook = createRandomDeniedBook();
        final String location = createDeniedBookAsUri(deniedBook);

        deniedBook.setId(Long.parseLong(location.split("api/denied/books/")[1]));
        deniedBook.setAuthor("newAuthor");
        Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(deniedBook)
            .put(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("newAuthor", response.jsonPath()
            .get("author"));

    }

    @Test
    public void whenDeleteCreatedDeniedBook_thenOk() {
        final DeniedBook deniedBook = createRandomDeniedBook();
        final String location = createDeniedBookAsUri(deniedBook);

        Response response = RestAssured.delete(location);
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = RestAssured.get(location);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    // ===============================

    private DeniedBook createRandomDeniedBook() {
        final DeniedBook deniedBook = new DeniedBook();
        deniedBook.setTitle(randomAlphabetic(10));
        deniedBook.setAuthor(randomAlphabetic(15));
        return deniedBook;
    }

    private String createDeniedBookAsUri(DeniedBook deniedBook) {
        final Response response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(deniedBook)
            .post(getApiRoot());
        return getApiRoot() + "/" + response.jsonPath()
            .get("id");
    }

}