package org.solovyev.android.messenger.vk.chats;

import android.content.Context;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.solovyev.android.messenger.MessengerConfigurationImpl;
import org.solovyev.android.messenger.chats.ApiChat;
import org.solovyev.android.messenger.chats.ChatMessage;
import org.solovyev.android.messenger.http.IllegalJsonException;
import org.solovyev.android.messenger.users.User;
import org.solovyev.android.messenger.vk.http.AbstractVkHttpTransaction;
import org.solovyev.android.messenger.vk.users.ApiUserField;
import org.solovyev.common.utils.StringUtils2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: serso
 * Date: 6/10/12
 * Time: 10:15 PM
 */

public class VkMessagesGetHistoryHttpTransaction extends AbstractVkHttpTransaction<List<ChatMessage>> {

    @NotNull
    private static final Integer MAX_COUNT = 100;

    @NotNull
    private Integer count;

    @Nullable
    private String chatId;

    @Nullable
    private Integer userId;

    @NotNull
    private User user;

    @Nullable
    private Integer offset;

    @NotNull
    private Context context;

    public VkMessagesGetHistoryHttpTransaction(@NotNull String chatId, @NotNull User user, @NotNull Context context) {
        super("messages.getHistory");
        this.chatId = chatId;
        this.user = user;
        this.context = context;
    }

    public VkMessagesGetHistoryHttpTransaction(@NotNull String chatId, @NotNull User user, @NotNull Integer offset, @NotNull Context context) {
        super("messages.getHistory");
        this.chatId = chatId;
        this.user = user;
        this.offset = offset;
        this.context = context;
    }

    public VkMessagesGetHistoryHttpTransaction(@NotNull Integer userId, @NotNull User user, @NotNull Context context) {
        super("messages.getHistory");
        this.userId = userId;
        this.user = user;
        this.context = context;
    }

    public VkMessagesGetHistoryHttpTransaction(@NotNull Integer userId, @NotNull User user, @NotNull Integer offset, @NotNull Context context) {
        super("messages.getHistory");
        this.userId = userId;
        this.user = user;
        this.offset = offset;
        this.context = context;
    }

    @NotNull
    @Override
    public List<NameValuePair> getRequestParameters() {
        final List<NameValuePair> requestParameters = super.getRequestParameters();

        requestParameters.add(new BasicNameValuePair("count", String.valueOf(count)));

        if (userId != null) {
            requestParameters.add(new BasicNameValuePair("uid", String.valueOf(userId)));
        }

        if (chatId != null) {
            requestParameters.add(new BasicNameValuePair("chat_id", chatId));
        }

        if (offset != null) {
            requestParameters.add(new BasicNameValuePair("offset", String.valueOf(offset)));
        }

        requestParameters.add(new BasicNameValuePair("fields", StringUtils2.getAllValues(Arrays.asList(ApiUserField.uid, ApiUserField.last_name))));

        return requestParameters;
    }

    @Override
    protected List<ChatMessage> getResponseFromJson(@NotNull String json) throws IllegalJsonException {
        final List<ApiChat> chats = new JsonChatConverter(user, chatId, userId, MessengerConfigurationImpl.getInstance().getServiceLocator().getUserService(), context).convert(json);

        // todo serso: optimize - convert json to the messages directly
        final List<ChatMessage> messages = new ArrayList<ChatMessage>(chats.size() * 10);
        for (ApiChat chat : chats) {
            messages.addAll(chat.getMessages());
        }

        return messages;
    }
}
