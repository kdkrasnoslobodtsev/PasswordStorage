package org.example.controllers;

import org.example.dao.requests.SignInRequest;
import org.example.dao.requests.SignUpRequest;
import org.example.dao.responses.JwtAuthenticationResponse;
import org.example.repositories.RecordRepository;
import org.example.repositories.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthenticationControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void resetDb() {
        recordRepository.flush();
    }


    @Test
    public void createUserTest() {
        SignUpRequest record = new SignUpRequest("Kirill", "Krasnoslobodtsev", "2003kirillka@gmail.com", "1234567");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("/api/v1/auth/signup", record, JwtAuthenticationResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void validSignInTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        SignInRequest signin = new SignInRequest("lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("/api/v1/auth/signin", signin, JwtAuthenticationResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void invalidSignInTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        SignInRequest signin = new SignInRequest("lyrics.red@yandex.ru", "123457");
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity("/api/v1/auth/signin", signin,JwtAuthenticationResponse.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
    }
}
