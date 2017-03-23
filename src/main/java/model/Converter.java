package model;

import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */

@Controller
public interface Converter {
    public String convert(String result) throws IOException;
    public String getDescription();
}
