package org.solovyev.android.messenger.realms.vk.users;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.solovyev.android.messenger.http.IllegalJsonException;
import org.solovyev.android.messenger.http.IllegalJsonRuntimeException;
import org.solovyev.android.messenger.realms.vk.VkAccount;
import org.solovyev.android.messenger.realms.vk.http.AbstractVkHttpTransaction;
import org.solovyev.android.messenger.users.User;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * User: serso
 * Date: 5/30/12
 * Time: 10:05 PM
 */
public class VkFriendsGetHttpTransaction extends AbstractVkHttpTransaction<List<User>> {

	@Nonnull
	private final String userId;

	private VkFriendsGetHttpTransaction(@Nonnull VkAccount realm, @Nonnull String userId) {
		super(realm, "friends.get");
		this.userId = userId;
	}

	@Nonnull
	public static VkFriendsGetHttpTransaction newInstance(@Nonnull VkAccount realm, @Nonnull String userId) {
		return new VkFriendsGetHttpTransaction(realm, userId);
	}

	@Override
	protected List<User> getResponseFromJson(@Nonnull String json) throws IllegalJsonException {
		try {
			return JsonUserConverter.newInstance(getAccount()).convert(json);
		} catch (IllegalJsonRuntimeException e) {
			throw e.getIllegalJsonException();
		}
	}

	@Nonnull
	@Override
	public List<NameValuePair> getRequestParameters() {
		final List<NameValuePair> result = super.getRequestParameters();

		result.add(new BasicNameValuePair("uid", userId));
		result.add(new BasicNameValuePair("fields", ApiUserField.getAllFieldsRequestParameter()));
		//result.add(new BasicNameValuePair("fields", ApiUserField.uid + "," + ApiUserField.first_name + "," + ApiUserField.last_name));

		return result;
	}
}
