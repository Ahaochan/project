package moe.ahao.spring.boot.xxl.job.properties;

public class XxlJobProperties {
    private String adminAddresses;
    private String accessToken;
    private String appName;
    private String address;
    private String ip;
    private int port;
    private String logPath;
    /**
     * 执行器日志文件保存天数
     */
    private int logRetentionDays;

    public String getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public int getLogRetentionDays() {
        return logRetentionDays;
    }

    public void setLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }
}
