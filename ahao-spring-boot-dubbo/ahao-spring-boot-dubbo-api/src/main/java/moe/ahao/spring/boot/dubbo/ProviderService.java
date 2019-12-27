package moe.ahao.spring.boot.dubbo;

public interface ProviderService {
    String V1_0_0 = "1.0.0";
    String LAST_VERSION = V1_0_0;

    String toUpperCase(String name);
}
