package model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by kunita.fumiko on 2017/03/22.
 */

@Slf4j
@Service

public class Processor {
    public String run(Converter converter ,String request) throws IOException {

        // これから行う処理を説明する(printDescriptionメソッドを呼び出す)
        printDescription(converter.getDescription());

        //文字列の変換処理
        String dest = converter.convert(request);
        //結果の出力処理
        return dest;
    }

    private void printDescription(String description) {
        System.out.println(description);
    }

    private String getSource() throws IOException {
        BufferedReader r = new BufferedReader(
                new InputStreamReader(System.in));
        return r.readLine();
    }

//    private void resultprint(String dest) {
//        System.out.println(dest);
//    }
}
