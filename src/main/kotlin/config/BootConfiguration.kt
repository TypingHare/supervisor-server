package supervisor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import supervisor.boot.ErrorCodeRangeAnalyzer

@Configuration(proxyBeanMethods = false)
class BootConfiguration {
    @Bean
    fun errorCodeRangeAnalysis() = ErrorCodeRangeAnalyzer()
}