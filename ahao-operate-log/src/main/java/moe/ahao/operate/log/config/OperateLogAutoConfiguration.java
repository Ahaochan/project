package moe.ahao.operate.log.config;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.operate.log.evaluator.OperateLogExpressionEvaluator;
import moe.ahao.operate.log.ifunc.DefaultFunctionServiceImpl;
import moe.ahao.operate.log.ifunc.IFunctionService;
import moe.ahao.operate.log.ifunc.IParseFunction;
import moe.ahao.operate.log.ifunc.ParseFunctionFactory;
import moe.ahao.operate.log.service.DefaultOperateLogStoreService;
import moe.ahao.operate.log.service.OperateLogStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class OperateLogAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IFunctionService.class)
    public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
        return new DefaultFunctionServiceImpl(parseFunctionFactory);
    }

    @Bean
    @ConditionalOnMissingBean(OperateLogStoreService.class)
    public OperateLogStoreService operateLogStoreService() {
        return new DefaultOperateLogStoreService();
    }

    @Bean
    public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
        return new ParseFunctionFactory(parseFunctions);
    }

    @Bean
    public OperateLogExpressionEvaluator operateLogExpressionEvaluator() {
        return new OperateLogExpressionEvaluator();
    }
}
