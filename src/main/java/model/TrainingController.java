package model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */
@Slf4j
@Controller
public class TrainingController {
    //リクエストボディ情報の取得
    @RequestMapping(value = "/kuni", method = RequestMethod.POST)
    public ResponseEntity<String> getProcessedCharacter(@RequestParam String convertType,
                                                        @RequestParam String character) throws IOException {
        log.debug("TrainingController");
        String[] request = {convertType, character};

        //Converterの取得
        Converter converter;
        switch (Integer.valueOf(request[0])) {
            case 0:
                converter = new DoublingConverter();
                break;
            case 1:
                converter = new NoDigitConverter();
                break;
            case 2:
                converter = new ToLowerConverter();
                break;
            default:
                throw new RuntimeException();
        }
        String result = new Processor().run(converter, request[1]);

        return ResponseEntity.ok(result);
    }
}