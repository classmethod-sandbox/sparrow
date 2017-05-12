package jp.classmethod.sparrow.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kunita.fumiko on 2017/05/12.
 */
@RunWith(MockitoJUnitRunner.class)
public class StringCheckTest {

    @InjectMocks
    StringCheck sut;

    @Test
    // 文字列が数字かどうかを確認します
    public void isNumberTest() {
        // setup
        String textNumber1 = "12";
        String textNumber2 = "-12";
        String textString = "あああああ";
        // exesice
        boolean resultNumber1 = sut.isNumber(textNumber1);
        boolean resultNumber2 = sut.isNumber(textNumber2);
        boolean resultString = sut.isNumber(textString);
        // verify
        assertThat(resultNumber1, is(true));
        assertThat(resultNumber2, is(true));
        assertThat(resultString, is(false));
    }
}
