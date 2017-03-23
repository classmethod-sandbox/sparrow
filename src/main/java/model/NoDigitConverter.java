package model;

import org.springframework.stereotype.Service;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */

@Service
public class NoDigitConverter extends AbstractConverter {
    @Override
    protected String computeStringToAppend(int c)  {
        StringBuilder sb = new StringBuilder();
        if ((Character.isDigit(c)) == false) {
            sb.append((char) c);
        }
        return sb.toString();
    }

    public String getDescription() {
        return "数字以外を除去します";
    }
}
