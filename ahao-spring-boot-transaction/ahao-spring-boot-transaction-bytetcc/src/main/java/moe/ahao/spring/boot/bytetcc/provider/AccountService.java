package moe.ahao.spring.boot.bytetcc.provider;

public interface AccountService {
    void increaseAmount(String accountId, double amount);
    void decreaseAmount(String accountId, double amount);
}
