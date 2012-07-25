package org.solovyev.android.messenger;

import android.database.sqlite.SQLiteOpenHelper;
import org.jetbrains.annotations.NotNull;
import org.solovyev.android.messenger.realms.Realm;
import org.solovyev.android.messenger.realms.UnsupportedRealmException;

/**
 * User: serso
 * Date: 5/24/12
 * Time: 9:36 PM
 */
public class MessengerConfigurationImpl implements MessengerConfiguration {

    @NotNull
    private String realmId;

    @NotNull
    private DaoLocator daoLocator;

    @NotNull
    private ServiceLocator serviceLocator;

    @NotNull
    private MessengerApiProvider messengerApiProvider;

    @NotNull
    private SQLiteOpenHelper sqliteOpenHelper;

    @NotNull
    private static final MessengerConfigurationImpl instance = new MessengerConfigurationImpl();

    @NotNull
    public static MessengerConfigurationImpl getInstance() {
        return instance;
    }

    private MessengerConfigurationImpl() {
    }

    @NotNull
    @Override
    public Realm getRealm() {
        try {
            return this.getServiceLocator().getRealmService().getRealmById(this.realmId);
        } catch (UnsupportedRealmException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public DaoLocator getDaoLocator() {
        return daoLocator;
    }

    public void setDaoLocator(@NotNull DaoLocator daoLocator) {
        this.daoLocator = daoLocator;
    }

    @NotNull
    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public void setServiceLocator(@NotNull ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public void setMessengerApiProvider(@NotNull MessengerApiProvider messengerApiProvider) {
        this.messengerApiProvider = messengerApiProvider;
    }

    @NotNull
    @Override
    public MessengerApi getMessengerApi() {
        return messengerApiProvider.getMessengerApi();
    }

    @NotNull
    public SQLiteOpenHelper getSqliteOpenHelper() {
        return sqliteOpenHelper;
    }

    public void setSqliteOpenHelper(@NotNull SQLiteOpenHelper sqliteOpenHelper) {
        this.sqliteOpenHelper = sqliteOpenHelper;
    }

    public void setRealmId(@NotNull String realmId) {
        this.realmId = realmId;
    }
}
