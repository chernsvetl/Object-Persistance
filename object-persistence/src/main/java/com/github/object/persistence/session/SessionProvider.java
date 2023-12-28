package com.github.object.persistence.session;

import com.github.object.persistence.core.ConfigDataSource;
import com.github.object.persistence.core.DbTypes;
import com.github.object.persistence.session.impl.SqlConnectionInstallerImpl;
import com.github.object.persistence.session.impl.SqlFactoryImpl;

import java.util.Map;

/**
 * Знает о всех фабриках и умеет доставать из них сесиии
 */
public class SessionProvider {
    private static final SessionProvider INSTANCE = new SessionProvider();
    private final Map<DbTypes, SessionFactory> factories = initFactories();

    private Map<DbTypes, SessionFactory> initFactories() {
        return Map.ofEntries(
                Map.entry(DbTypes.RELATIONAL, new SqlFactoryImpl(new SqlConnectionInstallerImpl()))
        );
    }

    public static SessionProvider getInstance() {
        return INSTANCE;
    }

    public Session createSession() {
        return decideSession().openSession();
    }

    public void shutdown(){
        decideSession().shutdown();
    }

    private SessionFactory decideSession() {
        SessionFactory factory = factories.get(ConfigDataSource.getInstance().getDataSourceType());
        if (factory == null) {
            String message = String.format("Factory with name %s is not present in provider", DbTypes.RELATIONAL.typeName);
            throw new IllegalStateException(message);
        }
        return factory;
    }
}
