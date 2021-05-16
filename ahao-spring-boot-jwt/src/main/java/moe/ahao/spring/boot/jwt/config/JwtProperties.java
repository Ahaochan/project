package moe.ahao.spring.boot.jwt.config;

import io.jsonwebtoken.CompressionCodec;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties("jwt")
public class JwtProperties {

    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    private Class<CompressionCodec> compressionCodec = null;

    private Map<String, Object> customHeader;

    private String signKey = "signKey";

    private Long expiration = 24 * 60 * 60 * 1000L;

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Class<CompressionCodec> getCompressionCodec() {
        return compressionCodec;
    }

    public void setCompressionCodec(Class<CompressionCodec> compressionCodec) {
        this.compressionCodec = compressionCodec;
    }

    public Map<String, Object> getCustomHeader() {
        return customHeader;
    }

    public void setCustomHeader(Map<String, Object> customHeader) {
        this.customHeader = customHeader;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
