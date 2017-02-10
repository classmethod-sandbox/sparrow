package jp.classmethod.sparrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Sparrowアプリケーションのエントリーポイント。
 *
 * <p>ベーシックなSpring bootアプリケーションクラス。</p>
 *
 * @author daisuke
 * @since #version#
 */
@SpringBootApplication
public class SparrowApplication {

	public static void main(String[] args) {
		SpringApplication.run(SparrowApplication.class, args);
	}
}
