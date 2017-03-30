package jp.classmethod.sparrow.configprops;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by mochizukimasao on 2017/03/30.
 *
 * @author mochizukimasao
 * @since version
 */

@Data
@Component
@ConfigurationProperties(prefix = "sparrow.bot")
public class LineBotConfigurationProperties {
	
	private String channelToken;
	
	private String channelSecret;
}
