package model;

import org.springframework.stereotype.Service;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */

@Service
public class ToLowerConverter extends AbstractConverter {
    protected String computeStringToAppend(int c)  {

        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase((char) c));
        return sb.toString();
    }

    public String getDescription() {
        return "すべて小文字に変換します";
    }
}
