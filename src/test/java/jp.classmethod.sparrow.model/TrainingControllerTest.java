package jp.classmethod.sparrow.model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import model.TrainingController;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by kunita.fumiko on 2017/03/23.
 */

@RunWith(MockitoJUnitRunner.class)
public class TrainingControllerTest {

    @InjectMocks
    TrainingController sut;

    private MockMvc mvc;

    @Before
    public void setup() {mvc = MockMvcBuilders.standaloneSetup(sut).build(); }

    //POSTでリクエストボディを取得する練習
    @Test
    public void testGetIndex2() throws Exception {
        // exercise
        mvc.perform(post("/kuni")
                .param("convertType", "1")
                .param("character", "aDt112dQ"))	//paramメソッドを使ってリクエストパラメータの指定
                // verify
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("aDtdQ"));
    }
}
