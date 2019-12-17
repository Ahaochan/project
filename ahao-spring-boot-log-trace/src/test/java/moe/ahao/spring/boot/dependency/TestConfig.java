package moe.ahao.spring.boot.dependency;

import com.ahao.util.spring.mq.RabbitMQHelper;
import moe.ahao.spring.boot.log.mq.RabbitMDCPublishPostProcessor;
import moe.ahao.spring.boot.log.mq.RabbitMDCReceivePostProcessor;
import moe.ahao.spring.boot.log.thread.MDCTaskDecorator;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class TestConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        return executor();
    }

    @Bean("asyncExecutor")
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setTaskDecorator(new MDCTaskDecorator());
        return executor;
        // return Executors.newFixedThreadPool(2);
    }

    @Bean(RabbitMQHelper.DELAY_EXCHANGE_NAME)
    public Exchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQHelper.DELAY_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public BeanPostProcessor rabbitBeanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof RabbitTemplate) {
                    warpRabbitTemplate((RabbitTemplate) bean, beanName);
                }
                if (bean instanceof AbstractRabbitListenerContainerFactory) {
                    warpContainerFactory((AbstractRabbitListenerContainerFactory) bean, beanName);
                }
                return bean;
            }

            private void warpRabbitTemplate(RabbitTemplate bean, String beanName) {
                bean.setBeforePublishPostProcessors(new RabbitMDCPublishPostProcessor());
                bean.setAfterReceivePostProcessors(new RabbitMDCReceivePostProcessor());
            }

            private void warpContainerFactory(AbstractRabbitListenerContainerFactory bean, String beanName) {
                bean.setAfterReceivePostProcessors(new RabbitMDCReceivePostProcessor());
            }
        };
    }
}
