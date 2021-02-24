package moe.ahao.spring.boot.dubbo.service;

import moe.ahao.spring.boot.dubbo.ProviderService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = ProviderService.V1_0_0)
public class ProviderServiceV1_0_0 implements ProviderService {

    @Override
    public String toUpperCase(String name) {
        return name.toUpperCase();
    }
}
