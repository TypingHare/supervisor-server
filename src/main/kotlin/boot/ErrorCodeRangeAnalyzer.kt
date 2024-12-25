package supervisor.boot

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter
import supervisor.exception.ServiceException
import supervisor.exception.annotation.ErrorCodeRange

class ErrorCodeRangeAnalyzer : BeanDefinitionRegistryPostProcessor,
    InitializingBean {
    private val classes = mutableListOf<Class<*>>()
    private val internalNodeClasses = mutableSetOf<Class<*>>()

    override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
        val scanner = ClassPathScanningCandidateComponentProvider(false).apply {
            addIncludeFilter(AssignableTypeFilter(ServiceException::class.java))
        }

        scanner.findCandidateComponents("supervisor")
            .forEach { candidateComponent ->
                val className = candidateComponent.beanClassName
                val serviceExceptionClass = Class.forName(className)
                val errorCodeRange =
                    getErrorCodeRangeAnnotation(serviceExceptionClass)
                if (errorCodeRange == null) {
                    logger.warn("The service exception class $className is not annotated by $ERROR_CODE_RANGE_CLASS_NAME")
                    return@forEach
                }

                classes.add(serviceExceptionClass)
            }
    }

    override fun afterPropertiesSet() {
        logger.info("Analyzing service exception classes")
        classes.forEach { analyze(it) }
    }

    private fun getErrorCodeRangeAnnotation(
        serviceExceptionClass: Class<*>
    ): ErrorCodeRange? =
        serviceExceptionClass.getAnnotation(ErrorCodeRange::class.java)

    private fun analyze(serviceExceptionClass: Class<*>) {
        val errorCodeRange = getErrorCodeRangeAnnotation(serviceExceptionClass)
        requireNotNull(errorCodeRange)
        internalNodeClasses.add(serviceExceptionClass)

        if (serviceExceptionClass == ServiceException::class.java) {
            return
        }

        val superClass = serviceExceptionClass.superclass
        internalNodeClasses.add(superClass)

        val thisErrorCodeRange =
            getErrorCodeRangeAnnotation(serviceExceptionClass)!!
        val superErrorCodeRange = getErrorCodeRangeAnnotation(superClass)!!
        if (thisErrorCodeRange.start < superErrorCodeRange.start || thisErrorCodeRange.end > superErrorCodeRange.end) {
            logger.warn("${serviceExceptionClass.name} extends ${superClass.name}, but the code range is not contained")
        }
    }

    companion object {
        val ERROR_CODE_RANGE_CLASS_NAME: String =
            ErrorCodeRange::class.java.name
        val logger =
            LoggerFactory.getLogger(ErrorCodeRangeAnalyzer::class.java)!!
    }
}