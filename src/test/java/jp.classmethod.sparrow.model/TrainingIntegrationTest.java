package jp.classmethod.sparrow.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by kunita.fumiko on 2017/03/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TrainingIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;


    @Test
    public void testCharacterProcessing() {
        // setup
        HttpHeaders headers = new HttpHeaders();
        //追加
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("convertType", "1");
        data.add("character", "aDt112dQ");
        HttpEntity<Object> entity = new HttpEntity<>(data, headers);
        // exercise
        ResponseEntity<String> actual = restTemplate.exchange("/converter", HttpMethod.POST, entity, String.class);
        // verify
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo("aDtdQ");
    }
}
