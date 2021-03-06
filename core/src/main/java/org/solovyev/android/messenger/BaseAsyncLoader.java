/*
 * Copyright 2013 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.solovyev.android.messenger;

import android.content.Context;
import android.widget.Filter;
import org.solovyev.android.list.ListAdapter;
import org.solovyev.android.list.ListItem;
import org.solovyev.android.messenger.api.MessengerAsyncTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public abstract class BaseAsyncLoader<R, LI extends ListItem> extends MessengerAsyncTask<Void, Void, List<R>> {

	@Nonnull
	private final WeakReference<ListAdapter<LI>> adapterRef;

	@Nullable
	private Runnable onPostExecute;

	public BaseAsyncLoader(@Nonnull Context context,
						   @Nonnull ListAdapter<LI> adapter,
						   @Nullable Runnable onPostExecute) {
		super(context);
		this.adapterRef = new WeakReference<ListAdapter<LI>>(adapter);
		this.onPostExecute = onPostExecute;
	}


	@Override
	protected List<R> doWork(@Nonnull List<Void> voids) {
		final Context context = getContext();
		if (context != null) {
			return getElements(context);
		}

		return Collections.emptyList();
	}

	@Nonnull
	protected abstract List<R> getElements(@Nonnull Context context);

	@Override
	protected void onSuccessPostExecute(@Nullable final List<R> elements) {
		final ListAdapter<LI> adapter = adapterRef.get();
		if (adapter != null) {
			if (elements != null && !elements.isEmpty()) {
				adapter.doWork(new Runnable() {
					@Override
					public void run() {
						adapter.clear();
						for (R element : elements) {
							adapter.add(createListItem(element));
						}
					}
				});
			} else {
				adapter.clear();
			}

			if (onPostExecute != null) {
				adapter.getFilter().filter(adapter.getFilterText(), new Filter.FilterListener() {
					@Override
					public void onFilterComplete(int count) {
						onPostExecute.run();
					}
				});
			}
		}
	}

	@Nullable
	public ListAdapter<LI> getAdapter() {
		return adapterRef.get();
	}

	@Nonnull
	protected abstract LI createListItem(@Nonnull R element);
}
