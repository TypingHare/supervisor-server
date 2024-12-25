package supervisor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import supervisor.common.constant.ContextKey
import supervisor.common.constant.HeadersKey

@Configuration
class RequestHeadersConfiguration {
    @Bean
    fun headerToContextKeyMap(): Map<String, String> = mapOf(
        HeadersKey.AUTHORIZATION to ContextKey.TOKEN,
    )
}