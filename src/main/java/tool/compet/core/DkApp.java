///*
// * Copyright (c) 2017-2021 DarkCompet. All rights reserved.
// */
//
//package tool.compet.core;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.Context;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.lifecycle.ViewModelStore;
//import androidx.lifecycle.ViewModelStoreOwner;
//
//import java.util.Locale;
//
//import compet.bundle.BuildConfig;
//import tool.compet.topic.DkTopicManager;
//import tool.compet.topic.DkTopicProvider;
//import tool.compet.topic.TheTopic;
//
//// Single application (lite version compares with multidex app).
//public class DkApp extends Application implements TheApp, DkTopicProvider {
//	protected static Context appContext;
//
//	// This holds ViewModel objects which can overcome while config-change
//	protected ViewModelStore viewModelStore;
//
//	@Override
//	public void onCreate() {
//		DkLogcats.tick(this, tool.compet.core.DkStrings.format("%s->onCreate()", getClass().getSimpleName()));
//
//		// We sync debug/release flag for Java and App
//		DkLogs.logger = DkLogcats.logger;
//
//		super.onCreate();
//		appContext = this;
//
//		// fixme This is good approach to singleton executor service? pls consider it...
//		DkExecutorService.install();
//
//		// For Test・Analyze at debug mode
//		if (BuildConfig.DEBUG) {
//			DkLogcats.info(this, "-----> Current device id: %s", DkConfig.deviceId(this));
//
//			registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//				@Override
//				public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//					DkLogcats.tick(this, tool.compet.core.DkStrings.format("%s->onCreate()", activity.getClass().getSimpleName()));
//				}
//
//				@Override
//				public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//				}
//
//				@Override
//				public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//					DkLogcats.tock(this);
//				}
//
//				@Override
//				public void onActivityPreStarted(@NonNull Activity activity) {
//					DkLogcats.tick(this, DkStrings.format("%s->onStart()", activity.getClass().getSimpleName()));
//				}
//
//				@Override
//				public void onActivityStarted(@NonNull Activity activity) {
//				}
//
//				@Override
//				public void onActivityPostStarted(@NonNull Activity activity) {
//					DkLogcats.tock(this);
//				}
//
//				@Override
//				public void onActivityResumed(@NonNull Activity activity) {
//				}
//
//				@Override
//				public void onActivityPaused(@NonNull Activity activity) {
//				}
//
//				@Override
//				public void onActivityStopped(@NonNull Activity activity) {
//				}
//
//				@Override
//				public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
//				}
//
//				@Override
//				public void onActivityDestroyed(@NonNull Activity activity) {
//				}
//			});
//
//			DkLogcats.tock(this);
//		}
//	}
//
//	// This makes the app become view model store owner
//	@NonNull
//	@Override
//	public ViewModelStore getViewModelStore() {
//		return viewModelStore != null ? viewModelStore : (viewModelStore = new ViewModelStore());
//	}
//
//	/**
//	 * Obtain a topic at `app` scope.
//	 */
//	@Override
//	public <T extends TheTopic<?>> T topic(String topicId, Class<T> topicType) {
//		return new DkTopicManager<>(this, topicId, topicType).registerClient(this, false);
//	}
//
//	@Override
//	public <T extends TheTopic<?>> T topic(String topicId, Class<T> topicType, ViewModelStoreOwner scope) {
//		return new DkTopicManager<>(scope, topicId, topicType).registerClient(this, false);
//	}
//
//	/**
//	 * Should NOT use this app context to inflate a view since it maybe not support attributes for View.
//	 */
//	public static Context context() {
//		return appContext;
//	}
//
//	/**
//	 * App locale based on current context.
//	 * If you have changed (wrapped) localed-context, then new locale was made.
//	 */
//	public static Locale locale() {
//		return DkConfig.appLocale(appContext);
//	}
//
//	/**
//	 * App language based on current context.
//	 * If you have changed (wrapped) localed-context, then new language was made.
//	 */
//	public static String lang() {
//		return locale().getLanguage();
//	}
//
//	/**
//	 * Quit current app with a status.
//	 */
//	public static void quit(int status) {
//		System.exit(status);
//	}
//}
