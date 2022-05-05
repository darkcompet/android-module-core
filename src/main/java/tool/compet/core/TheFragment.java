/*
 * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
 */

package tool.compet.core;

import androidx.fragment.app.Fragment;

/**
 * Fragment interface, a fragment can implement this to work with Dk library.
 */
public interface TheFragment {
	/**
	 * Obtain fragment itself.
	 */
	Fragment getFragment();

	/**
	 * ID of layout resource for this fragment, for eg,. `R.layout.frag_home`
	 */
	int layoutResourceId();

	/**
	 * ID of container inside the layout of this fragment. This id can be used in
	 * fragment transaction for other screens, for eg,. `R.id.frag_container`
	 */
	int fragmentContainerId();

	/**
	 * Each fragment should response `onBackPressed()` from host activity when user pressed physical back button.
	 *
	 * @return true if this fragment will handle this event, otherwise false.
	 */
	boolean onBackPressed();

	/**
	 * Dismiss (finish) itself.
	 *
	 * @return true if finish succeed, otherwise failed.
	 */
	boolean close();
}
