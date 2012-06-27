package org.solovyev.android.messenger.chats;

import android.content.Context;
import android.database.Cursor;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.solovyev.android.messenger.users.UserService;
import org.solovyev.common.utils.Converter;

/**
 * User: serso
 * Date: 6/9/12
 * Time: 10:15 PM
 */
public class ChatMessageMapper implements Converter<Cursor, ChatMessage> {

    @NotNull
    private final UserService userService;

    @NotNull
    private final Context context;

    public ChatMessageMapper(@NotNull UserService userService, @NotNull Context context) {
        this.userService = userService;
        this.context = context;
    }

    @NotNull
    @Override
    public ChatMessage convert(@NotNull Cursor c) {
        final Integer messageId = c.getInt(0);

        final LiteChatMessageImpl liteChatMessage = LiteChatMessageImpl.newInstance(messageId);
        liteChatMessage.setAuthor(userService.getUserById(c.getInt(2), context));
        if (!c.isNull(3)) {
            int recipientId = c.getInt(3);
            liteChatMessage.setRecipient(userService.getUserById(recipientId, context));
        }
        final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.basicDateTime();

        liteChatMessage.setSendDate(dateTimeFormatter.parseDateTime(c.getString(4)));
        liteChatMessage.setTitle(c.getString(5));
        liteChatMessage.setBody(c.getString(6));

        return ChatMessageImpl.newInstance(liteChatMessage);
    }
}
