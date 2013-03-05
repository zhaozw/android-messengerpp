package org.solovyev.android.messenger.realms.vk.users;

import android.content.Context;
import org.solovyev.android.http.HttpRuntimeIoException;
import org.solovyev.android.http.HttpTransactions;
import org.solovyev.android.messenger.realms.Realm;
import org.solovyev.android.messenger.realms.vk.R;
import org.solovyev.android.messenger.users.Gender;
import org.solovyev.android.messenger.users.RealmUserService;
import org.solovyev.android.messenger.users.User;
import org.solovyev.android.properties.AProperty;
import org.solovyev.android.properties.APropertyImpl;
import org.solovyev.common.collections.Collections;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* User: serso
* Date: 5/28/12
* Time: 1:18 PM
*/
public class VkRealmUserService implements RealmUserService {

    @Nonnull
    private final Realm realm;

    public VkRealmUserService(@Nonnull Realm realm) {
        this.realm = realm;
    }

    @Override
    public User getUserById(@Nonnull String realmUserId) {
        try {
            final List<User> users = HttpTransactions.execute(VkUsersGetHttpTransaction.newInstance(realm, realmUserId, null));
            return Collections.getFirstListElement(users);
        } catch (IOException e) {
            throw new HttpRuntimeIoException(e);
        }
    }

    @Nonnull
    @Override
    public List<User> getUserContacts(@Nonnull String realmUserId) {
        try {
            return HttpTransactions.execute(VkFriendsGetHttpTransaction.newInstance(realm, realmUserId));
        } catch (IOException e) {
            throw new HttpRuntimeIoException(e);
        }
    }


    @Nonnull
    @Override
    public List<User> checkOnlineUsers(@Nonnull List<User> users) {
        final List<User> result = new ArrayList<User>(users.size());

        try {
            for (VkUsersGetHttpTransaction vkUsersGetHttpTransaction : VkUsersGetHttpTransaction.newInstancesForUsers(realm, users, Arrays.asList(ApiUserField.online))) {
                result.addAll(HttpTransactions.execute(vkUsersGetHttpTransaction));
            }
        } catch (IOException e) {
            throw new HttpRuntimeIoException(e);
        }

        return result;
    }

    @Nonnull
    @Override
    public List<AProperty> getUserProperties(@Nonnull User user, @Nonnull Context context) {
        final List<AProperty> result = new ArrayList<AProperty>(user.getProperties().size());

        for (AProperty property : user.getProperties()) {
            final String name = property.getName();
            if ( name.equals(User.PROPERTY_NICKNAME) ) {
                result.add(APropertyImpl.newInstance(context.getString(R.string.mpp_nickname), property.getValue()));
            } else if ( name.equals(User.PROPERTY_SEX) ) {
                result.add(APropertyImpl.newInstance(context.getString(R.string.mpp_sex), context.getString(Gender.valueOf(property.getValue()).getCaptionResId())));
            } else if ( name.equals("bdate") ) {
                result.add(APropertyImpl.newInstance(context.getString(R.string.mpp_birth_date), property.getValue()));
            } else if ( name.equals("countryId") ) {
                result.add(APropertyImpl.newInstance(context.getString(R.string.mpp_country), property.getValue()));
            } else if ( name.equals("cityId") ) {
                result.add(APropertyImpl.newInstance(context.getString(R.string.mpp_city), property.getValue()));
            }
            
        }
        
        return result;
    }
}
