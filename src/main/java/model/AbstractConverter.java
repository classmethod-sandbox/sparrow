package model;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */

import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.io.StringReader;

@Controller
public abstract class AbstractConverter implements Converter {
    public final String convert(String request) throws IOException {
        StringBuilder sb = new StringBuilder();
        StringReader reader = new StringReader(request);
        int c;
        while ((c = reader.read()) != -1) {
            String str = computeStringToAppend(c);
            if (str != null) {
                sb.append(str);
            }
        }
        return sb.toString();
    }
    protected abstract String
    computeStringToAppend(int c);
}
