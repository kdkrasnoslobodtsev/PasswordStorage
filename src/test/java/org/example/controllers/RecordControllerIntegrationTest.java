package org.example.controllers;

import org.example.dao.requests.CreateUpdateRecordRequest;
import org.example.dao.requests.SignUpRequest;
import org.example.dao.responses.JwtAuthenticationResponse;
import org.example.dao.responses.RecordResponse;
import org.example.repositories.RecordRepository;
import org.example.repositories.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RecordControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    @After
    public void resetDb() {
        recordRepository.flush();
    }

    @Test
    public void createRecordTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        CreateUpdateRecordRequest record = new CreateUpdateRecordRequest("leetcode", "lyrics.red@yandex.ru", "1234567", "https://leetcode.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + signUpResponse.getBody().getToken());
        ResponseEntity<RecordResponse> postResponse = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(record, headers), RecordResponse.class);
        ResponseEntity<RecordResponse> getResponse = restTemplate.exchange("/api/v1/record/" + postResponse.getBody().getId().toString(), HttpMethod.GET, new HttpEntity<>(headers), RecordResponse.class);
        assertThat(postResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(getResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(getResponse.getBody().getName(), is("leetcode"));
    }

    @Test
    public void findRecordByIdTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        CreateUpdateRecordRequest record = new CreateUpdateRecordRequest("leetcode", "lyrics.red@yandex.ru", "1234567", "https://leetcode.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + signUpResponse.getBody().getToken());
        ResponseEntity<RecordResponse> postResponse = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(record, headers), RecordResponse.class);
        ResponseEntity<RecordResponse> getResponse = restTemplate.exchange("/api/v1/record/" + postResponse.getBody().getId().toString(), HttpMethod.GET, new HttpEntity<>(headers), RecordResponse.class);
        assertThat(getResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(getResponse.getBody().getName(), is("leetcode"));
    }

    @Test
    public void invalidFindRecordByIdTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        CreateUpdateRecordRequest record = new CreateUpdateRecordRequest("leetcode", "lyrics.red@yandex.ru", "1234567", "https://leetcode.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + signUpResponse.getBody().getToken());
        ResponseEntity<RecordResponse> postResponse = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(record, headers), RecordResponse.class);
        ResponseEntity<String> getResponse = restTemplate.exchange("/api/v1/record/" + UUID.randomUUID(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertThat(getResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void updateRecordByIdTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        CreateUpdateRecordRequest post = new CreateUpdateRecordRequest("leetcode", "lyrics.red@yandex.ru", "1234567", "https://leetcode.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + signUpResponse.getBody().getToken());
        ResponseEntity<RecordResponse> postResponse = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post, headers), RecordResponse.class);
        CreateUpdateRecordRequest put = new CreateUpdateRecordRequest("codeforces", "2003kirillka@gmail.com", "12345", "https://codeforces.com");
        ResponseEntity<RecordResponse> putResponse = restTemplate.exchange("/api/v1/record/" + postResponse.getBody().getId().toString(), HttpMethod.PUT, new HttpEntity<>(put, headers), RecordResponse.class);
        ResponseEntity<RecordResponse> getResponse = restTemplate.exchange("/api/v1/record/" + postResponse.getBody().getId().toString(), HttpMethod.GET, new HttpEntity<>(headers), RecordResponse.class);
        assertThat(getResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(getResponse.getBody().getName(), is("codeforces"));
    }

    @Test
    public void invalidUpdateRecordByIdTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        CreateUpdateRecordRequest post = new CreateUpdateRecordRequest("leetcode", "lyrics.red@yandex.ru", "1234567", "https://leetcode.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + signUpResponse.getBody().getToken());
        ResponseEntity<RecordResponse> postResponse = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post, headers), RecordResponse.class);
        CreateUpdateRecordRequest put = new CreateUpdateRecordRequest("codeforces", "2003kirillka@gmail.com", "12345", "https://codeforces.com");
        ResponseEntity<String> putResponse = restTemplate.exchange("/api/v1/record/" + UUID.randomUUID(), HttpMethod.PUT, new HttpEntity<>(put, headers), String.class);
        assertThat(putResponse.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void findAllRecordsTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        CreateUpdateRecordRequest post1 = new CreateUpdateRecordRequest("leetcode", "lyrics.red@yandex.ru", "1234567", "https://leetcode.com");
        CreateUpdateRecordRequest post2 = new CreateUpdateRecordRequest("codeforces", "lyrics.red@yandex.ru", "1234567", "https://codeforces.com");
        CreateUpdateRecordRequest post3 = new CreateUpdateRecordRequest("javarush", "lyrics.red@yandex.ru", "1234567", "https://javarush.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + signUpResponse.getBody().getToken());
        ResponseEntity<RecordResponse> postResponse1 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post1, headers), RecordResponse.class);
        ResponseEntity<RecordResponse> postResponse2 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post2, headers), RecordResponse.class);
        ResponseEntity<RecordResponse> postResponse3 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post3, headers), RecordResponse.class);
        ResponseEntity<List<RecordResponse>> getResponse = restTemplate.exchange("/api/v1/record", HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<RecordResponse>>() {});
        assertThat(getResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(getResponse.getBody().size(), is(3));
        assertThat(getResponse.getBody().get(0).getName(), is("leetcode"));
        assertThat(getResponse.getBody().get(1).getName(), is("codeforces"));
        assertThat(getResponse.getBody().get(2).getName(), is("javarush"));
    }

    @Test
    public void invalidRecordCreateTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        CreateUpdateRecordRequest post1 = new CreateUpdateRecordRequest("leetcode", "lyrics.red@yandex.ru", null, "https://leetcode.com");
        CreateUpdateRecordRequest post2 = new CreateUpdateRecordRequest(null, "lyrics.red@yandex.ru", "123456", "https://leetcode.com");
        CreateUpdateRecordRequest post3 = new CreateUpdateRecordRequest(null, "lyrics.red@yandex.ru", null, "https://leetcode.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + signUpResponse.getBody().getToken());
        ResponseEntity<String> postResponse1 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post1, headers), String.class);
        ResponseEntity<String> postResponse2 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post2, headers), String.class);
        ResponseEntity<String> postResponse3 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post3, headers), String.class);
        assertThat(postResponse1.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(postResponse2.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(postResponse3.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void paginationTest() {
        SignUpRequest signup = new SignUpRequest("Kirill", "Krasnoslobodtsev", "lyrics.red@yandex.ru", "123456");
        ResponseEntity<JwtAuthenticationResponse> signUpResponse = restTemplate.postForEntity("/api/v1/auth/signup", signup, JwtAuthenticationResponse.class);
        CreateUpdateRecordRequest post1 = new CreateUpdateRecordRequest("leetcode", "lyrics.red@yandex.ru", "123456", "https://leetcode.com");
        CreateUpdateRecordRequest post2 = new CreateUpdateRecordRequest("codeforces", "lyrics.red@yandex.ru", "123456", "https://codeforces.com");
        CreateUpdateRecordRequest post3 = new CreateUpdateRecordRequest("javarush", "lyrics.red@yandex.ru", "123456", "https://javarush.com");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + " " + signUpResponse.getBody().getToken());
        ResponseEntity<String> postResponse1 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post1, headers), String.class);
        ResponseEntity<String> postResponse2 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post2, headers), String.class);
        ResponseEntity<String> postResponse3 = restTemplate.exchange("/api/v1/record", HttpMethod.POST, new HttpEntity<>(post3, headers), String.class);
        ResponseEntity<List<RecordResponse>> getResponse1 = restTemplate.exchange("/api/v1/record?pageSize=2&pageNum=1", HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<RecordResponse>>() {});
        ResponseEntity<List<RecordResponse>> getResponse2 = restTemplate.exchange("/api/v1/record?pageSize=2&pageNum=2", HttpMethod.GET, new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<RecordResponse>>() {});
        assertThat(getResponse1.getStatusCode(), is(HttpStatus.OK));
        assertThat(getResponse2.getStatusCode(), is(HttpStatus.OK));
        assertThat(getResponse1.getBody().size(), is(2));
        assertThat(getResponse2.getBody().size(), is(1));
        assertThat(getResponse1.getBody().get(0).getName(), is("leetcode"));
        assertThat(getResponse1.getBody().get(1).getName(), is("codeforces"));
        assertThat(getResponse2.getBody().get(0).getName(), is("javarush"));
    }
}
