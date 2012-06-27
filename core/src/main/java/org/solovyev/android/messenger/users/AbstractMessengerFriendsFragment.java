package org.solovyev.android.messenger.users;

import org.jetbrains.annotations.NotNull;
import org.solovyev.android.messenger.AbstractMessengerListFragment;
import org.solovyev.android.view.ListViewAwareOnRefreshListener;

/**
 * User: serso
 * Date: 6/2/12
 * Time: 5:11 PM
 */
public abstract class AbstractMessengerFriendsFragment extends AbstractMessengerListFragment<User> {

    @NotNull
    private static String TAG = "FriendsFragment";

    public AbstractMessengerFriendsFragment() {
        super(TAG);
    }

    @Override
    protected ListViewAwareOnRefreshListener getBottomPullRefreshListener() {
        return null;
    }

    @Override
    protected boolean isFilterEnabled() {
        return true;
    }
}
