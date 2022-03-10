package moe.ahao.spring.boot.bytetcc;

/**
 * copy from {@link org.bytesoft.bytetcc.supports.springboot.config.SpringBootSecondaryConfiguration}
 * https://github.com/liuyangming/ByteTCC/issues/121#issuecomment-907626084
 */

import org.apache.commons.lang3.StringUtils;
import org.bytesoft.bytetcc.*;
import org.bytesoft.bytetcc.supports.spring.SpringContextRegistry;
import org.bytesoft.bytetcc.supports.springboot.SpringBootBeanRegistry;
import org.bytesoft.bytetcc.supports.springboot.web.CompensableHandlerInterceptor;
import org.bytesoft.bytetcc.supports.springboot.web.CompensableRequestInterceptor;
import org.bytesoft.common.utils.CommonUtils;
import org.bytesoft.compensable.CompensableBeanFactory;
import org.bytesoft.compensable.aware.CompensableBeanFactoryAware;
import org.bytesoft.compensable.aware.CompensableEndpointAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ImportResource({ "classpath:bytetcc-disable-tx-advice.xml", "classpath:bytetcc-supports-springboot-secondary.xml" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
public class SpringBootSecondaryConfiguration
    implements TransactionManagementConfigurer, WebMvcConfigurer, SmartInitializingSingleton, InitializingBean,
    CompensableEndpointAware, EnvironmentAware, ApplicationContextAware, CompensableBeanFactoryAware {
    private ApplicationContext applicationContext;
    private String identifier;
    private Environment environment;
    private CompensableBeanFactory beanFactory;

    public void afterSingletonsInstantiated() {
        CompensableManagerImpl compensableManager = this.applicationContext.getBean(CompensableManagerImpl.class);
        CompensableCoordinator compensableCoordinator = this.applicationContext.getBean(CompensableCoordinator.class);
        UserCompensableImpl userCompensable = this.applicationContext.getBean(UserCompensableImpl.class);
        TransactionRecoveryImpl compensableRecovery = this.applicationContext.getBean(TransactionRecoveryImpl.class);
        compensableManager.setStatefully(true);
        compensableCoordinator.setStatefully(true);
        userCompensable.setStatefully(true);
        compensableRecovery.setStatefully(true);
    }

    public void afterPropertiesSet() throws Exception {
        this.initializeEndpointIfNecessary();
    }

    public void initializeEndpointIfNecessary() {
        if (StringUtils.isBlank(this.identifier)) {
            String host = CommonUtils.getInetAddress();
            String name = this.environment.getProperty("spring.application.name");
            String port = this.environment.getProperty("server.port");
            this.identifier = String.format("%s:%s:%s", host, name, port);
        }
    }

    public TransactionManager annotationDrivenTransactionManager() {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(this.applicationContext.getBean(TransactionManagerImpl.class));
        jtaTransactionManager.setUserTransaction(this.applicationContext.getBean(UserCompensableImpl.class));

        SpringContextRegistry springContextRegistry = SpringContextRegistry.getInstance();
        springContextRegistry.setApplicationContext(this.applicationContext);
        springContextRegistry.setBeanFactory(this.beanFactory);
        springContextRegistry.setTransactionManager(jtaTransactionManager);
        return springContextRegistry.getTransactionManager();
    }

    @org.springframework.context.annotation.Bean("jtaTransactionManager")
    public TransactionManager jtaTransactionManager() {
        return SpringContextRegistry.getInstance().getTransactionManager();
    }

    @org.springframework.context.annotation.Bean
    public CompensableHandlerInterceptor compensableHandlerInterceptor() {
        return new CompensableHandlerInterceptor();
    }

    @org.springframework.context.annotation.Bean
    public CompensableRequestInterceptor compensableRequestInterceptor() {
        return new CompensableRequestInterceptor();
    }

    @SuppressWarnings("deprecation")
    @org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean(ClientHttpRequestFactory.class)
    @org.springframework.context.annotation.Bean
    public ClientHttpRequestFactory defaultRequestFactory() {
        return new org.springframework.http.client.Netty4ClientHttpRequestFactory();
    }

    @org.springframework.context.annotation.Bean
    public SpringBootBeanRegistry springBootBeanRegistry(@Autowired ClientHttpRequestFactory requestFactory) {
        SpringBootBeanRegistry springBootBeanRegistry = SpringBootBeanRegistry.getInstance();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);

        springBootBeanRegistry.setRestTemplate(restTemplate);

        return springBootBeanRegistry;
    }

    @org.springframework.context.annotation.Primary
    @org.springframework.context.annotation.Bean
    public RestTemplate restTemplate(@Autowired CompensableRequestInterceptor compensableRequestInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(compensableRequestInterceptor);
        return restTemplate;
    }

    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        CompensableHandlerInterceptor compensableHandlerInterceptor = //
            this.applicationContext.getBean(CompensableHandlerInterceptor.class);
        interceptorRegistry.addInterceptor(compensableHandlerInterceptor);
    }

    public CompensableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(CompensableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public String getEndpoint() {
        return this.identifier;
    }

    public void setEndpoint(String identifier) {
        this.identifier = identifier;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
