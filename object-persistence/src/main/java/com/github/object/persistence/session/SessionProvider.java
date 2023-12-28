package com.github.object.persistence.session;

import com.github.object.persistence.session.impl.SqlConnectionInstallerImpl;
import com.github.object.persistence.session.impl.SqlFactoryImpl;

/**
 * Знает о всех фабриках и умеет доставать из них сесиии
 */
public class SessionProvider {
    private static final SessionProvider INSTANCE = new SessionProvider();
    private final SessionFactory factory = new SqlFactoryImpl(new SqlConnectionInstallerImpl());

    public static SessionProvider getInstance() {
        return INSTANCE;
    }

    public Session createSession() {
        return factory.openSession();
    }

    public void shutdown(){
        factory.shutdown();
    }
}
