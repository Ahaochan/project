package moe.ahao.spring.boot.orika;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 用于初始化 {@link ma.glasnost.orika.impl.DefaultMapperFactory.MapperFactoryBuilder}
 */
@ConfigurationProperties("orika")
public class OrikaProperties {
    private boolean enabled = true;
    private Boolean useBuiltinConverters;
    private Boolean useAutoMapping;
    private Boolean mapNulls;
    private Boolean dumpStateOnException;
    private Boolean favorExtension;
    private Boolean captureFieldContext;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getUseBuiltinConverters() {
        return useBuiltinConverters;
    }

    public void setUseBuiltinConverters(Boolean useBuiltinConverters) {
        this.useBuiltinConverters = useBuiltinConverters;
    }

    public Boolean getUseAutoMapping() {
        return useAutoMapping;
    }

    public void setUseAutoMapping(Boolean useAutoMapping) {
        this.useAutoMapping = useAutoMapping;
    }

    public Boolean getMapNulls() {
        return mapNulls;
    }

    public void setMapNulls(Boolean mapNulls) {
        this.mapNulls = mapNulls;
    }

    public Boolean getDumpStateOnException() {
        return dumpStateOnException;
    }

    public void setDumpStateOnException(Boolean dumpStateOnException) {
        this.dumpStateOnException = dumpStateOnException;
    }

    public Boolean getFavorExtension() {
        return favorExtension;
    }

    public void setFavorExtension(Boolean favorExtension) {
        this.favorExtension = favorExtension;
    }

    public Boolean getCaptureFieldContext() {
        return captureFieldContext;
    }

    public void setCaptureFieldContext(Boolean captureFieldContext) {
        this.captureFieldContext = captureFieldContext;
    }
}
