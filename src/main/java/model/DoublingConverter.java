package model;

import org.springframework.stereotype.Service;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */

@Service
public class DoublingConverter extends AbstractConverter {
    @Override
    protected String computeStringToAppend(int c) {
        StringBuilder sb = new StringBuilder();
        sb.append((char) c);
        sb.append((char) c);
        return sb.toString();
    }

    //処理説明
    public String getDescription() {
        return "文字をダブらせます";
    }
}
