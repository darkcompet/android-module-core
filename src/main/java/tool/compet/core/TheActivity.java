/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import android.app.Activity;

/**
 * Activity interface, an activity should implement this interface to work where Dk library.
 */
public interface TheActivity {
	/**
	 * Obtain itself.
	 */
	Activity getActivity();

	/**
	 * Specify id of layout resource for this Activity, for eg,. `R.layout.activity_main`.
	 */
	int layoutResourceId();

	/**
	 * Specify id of container inside the layout of this fragment. This id can be used in
	 * fragment transaction for other screens.
	 */
	int fragmentContainerId();

	/**
	 * Dismiss, finish itself.
	 *
	 * @return true if finish succeed, otherwise falied.
	 */
	boolean close();
}
