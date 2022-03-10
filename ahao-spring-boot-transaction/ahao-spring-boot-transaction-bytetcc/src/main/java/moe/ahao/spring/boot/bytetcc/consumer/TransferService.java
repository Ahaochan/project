package moe.ahao.spring.boot.bytetcc.consumer;

public interface TransferService {
    void transfer(String sourceAccountId, String targetAccountId, double amount);
}
