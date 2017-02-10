package jp.classmethod.sparrow;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * サンプルの {@link CommandLineRunner} 実装。
 *
 * <p>ログに {@code Hello, world!} 及び、コマンドライン引数（あれば）を出力する。</p>
 *
 * @author daisuke
 * @since #version#
 */
@Slf4j
@Component
public class ExampleCommandlineRunner implements CommandLineRunner {

	@Override
	public void run(String... args) {
		log.info("Hello, world!");
		Arrays.stream(args).forEach(log::info);
	}
}
