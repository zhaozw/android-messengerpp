package org.solovyev.android.messenger.realms.xmpp;

import android.content.Context;
import com.google.inject.Singleton;
import org.jivesoftware.smack.SmackAndroid;
import org.solovyev.android.messenger.RealmConnection;
import org.solovyev.android.messenger.chats.RealmChatService;
import org.solovyev.android.messenger.realms.AbstractRealmDef;
import org.solovyev.android.messenger.realms.Realm;
import org.solovyev.android.messenger.realms.RealmBuilder;
import org.solovyev.android.messenger.realms.RealmConfiguration;
import org.solovyev.android.messenger.users.RealmUserService;
import org.solovyev.android.messenger.users.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * User: serso
 * Date: 2/24/13
 * Time: 8:09 PM
 */
@Singleton
public class XmppRealmDef extends AbstractRealmDef {

    @Nonnull
    static final String REALM_ID = "xmpp";

    public XmppRealmDef() {
        super(REALM_ID, R.string.mpp_xmpp_name, R.drawable.mpp_xmpp_icon, XmppRealmConfigurationFragment.class, XmppRealmConfiguration.class);
    }

    @Nonnull
    @Override
    public RealmUserService newRealmUserService(@Nonnull Realm realm) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Nonnull
    @Override
    public RealmChatService newRealmChatService(@Nonnull Realm realm) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Nonnull
    @Override
    public RealmConnection newRealmConnection(@Nonnull Realm realm, @Nonnull Context context) {
        return new XmppRealmConnection((XmppRealm) realm, context);
    }

    @Nonnull
    @Override
    public Realm newRealm(@Nonnull String realmId, @Nonnull User user, @Nonnull RealmConfiguration configuration) {
        return new XmppRealm(realmId, this, user, (XmppRealmConfiguration) configuration);
    }

    @Override
    @Nonnull
    public RealmBuilder newRealmBuilder(@Nonnull RealmConfiguration configuration, @Nullable Realm editedRealm) {
        return new XmppRealmBuilder(this, editedRealm, (XmppRealmConfiguration) configuration);
    }

    @Override
    public void init(@Nonnull Context context) {
        super.init(context);

        SmackAndroid.init(context);
    }
}