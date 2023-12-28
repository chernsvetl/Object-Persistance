package com.github.object.persistence.session.impl;

import com.github.object.persistence.config.ConfigDataSource;
import com.github.object.persistence.exception.InstallConnectionException;
import com.github.object.persistence.session.ConnectionInstaller;
import com.github.object.persistence.session.DataSourceWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SqlConnectionInstallerImpl implements ConnectionInstaller<Connection> {

    private final String url;
    private final Properties properties;

    public SqlConnectionInstallerImpl() {
        try {
            Class.forName(ConfigDataSource.getInstance().getDriver());
            this.url = ConfigDataSource.getInstance().getDataSourceUrl();
            Properties datasourceProperties = new Properties();
            datasourceProperties.setProperty("user", ConfigDataSource.getInstance().getUsername());
            datasourceProperties.setProperty("password", ConfigDataSource.getInstance().getPassword());
            this.properties = datasourceProperties;
        } catch (ClassNotFoundException exception) {
            throw new InstallConnectionException("The specified driver was not found", exception);
        }
    }

    @Override
    public DataSourceWrapper<Connection> installConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, properties);
            return new DataSourceWrapperImpl<>(connection);
        } catch (SQLException exception) {
            throw new InstallConnectionException("Exception while trying to connect to database", exception);
        }
    }
}
