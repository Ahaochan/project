package com.ahao.spring.boot.datasources.datasource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * COPY FROM {@link org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource}
 */
public abstract class AbstractRoutingDataSource extends AbstractDataSource implements InitializingBean {
	@Override
	public Connection getConnection() throws SQLException {
		return DataSourceUtils.getConnection(determineTargetDataSource());
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return determineTargetDataSource().getConnection(username, password);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		if (iface.isInstance(this)) {
			return (T) this;
		}
		return determineTargetDataSource().unwrap(iface);
	}

    @Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return (iface.isInstance(this) || determineTargetDataSource().isWrapperFor(iface));
	}

    protected abstract DataSource determineTargetDataSource();
}
