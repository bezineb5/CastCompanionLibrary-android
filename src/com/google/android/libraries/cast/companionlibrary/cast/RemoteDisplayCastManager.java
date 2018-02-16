package com.google.android.libraries.cast.companionlibrary.cast;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.MediaRouteDialogFactory;
import android.util.Log;

import com.google.android.gms.cast.ApplicationMetadata;
import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.cast.LaunchOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.CastException;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.NoConnectionException;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.TransientNetworkDisconnectionException;
import com.google.android.libraries.cast.companionlibrary.utils.LogUtils;

import static com.google.android.libraries.cast.companionlibrary.utils.LogUtils.LOGD;
import static com.google.android.libraries.cast.companionlibrary.utils.LogUtils.LOGE;

/**
 * Created by benjamin on 15/01/16.
 */
public class RemoteDisplayCastManager extends BaseCastManager {

    private static final String TAG = LogUtils.makeLogTag(RemoteDisplayCastManager.class);
    private static RemoteDisplayCastManager sInstance;

    private final Class<? extends CastRemoteDisplayLocalService> mCastRemoteDisplayLocalServiceClass;
    private final Class<? extends Activity> mActivityClass;

    protected RemoteDisplayCastManager(Context context,
                                       Class<? extends CastRemoteDisplayLocalService> castRemoteDisplayLocalServiceClass,
                                       Class<? extends Activity> activityClass,
                                       CastConfiguration castConfiguration) {
        super(context, castConfiguration);

        this.mCastRemoteDisplayLocalServiceClass = castRemoteDisplayLocalServiceClass;
        this.mActivityClass = activityClass;
    }

    public static synchronized RemoteDisplayCastManager initialize(Context context,
                                                                   Class<? extends CastRemoteDisplayLocalService> castRemoteDisplayLocalServiceClass,
                                                                   Class<? extends Activity> activityClass,
                                                          CastConfiguration castConfiguration) {
        if (sInstance == null) {
            LOGD(TAG, "New instance of DataCastManager is created");
            if (ConnectionResult.SUCCESS != GooglePlayServicesUtil
                    .isGooglePlayServicesAvailable(context)) {
                String msg = "Couldn't find the appropriate version of Google Play Services";
                LOGE(TAG, msg);
                throw new RuntimeException(msg);
            }
            sInstance = new RemoteDisplayCastManager(context,
                    castRemoteDisplayLocalServiceClass,
                    activityClass,
                    castConfiguration);
        }
        return sInstance;
    }

    /**
     * Returns a (singleton) instance of this class. Clients should call this method in order to
     * get a hold of this singleton instance. If it is not initialized yet, a
     * {@link CastException} will be thrown.
     */
    public static RemoteDisplayCastManager getInstance() {
        if (sInstance == null) {
            String msg = "No RemoteDisplayCastManager instance was found, did you forget to initialize it?";
            LOGE(TAG, msg);
            throw new IllegalStateException(msg);
        }
        return sInstance;
    }

    @Override
    protected Cast.CastOptions.Builder getCastOptionBuilder(CastDevice device) {
        Cast.CastOptions.Builder builder = Cast.CastOptions.builder(
                mSelectedCastDevice, new Cast.Listener());

        if (isFeatureEnabled(CastConfiguration.FEATURE_DEBUGGING)) {
            builder.setVerboseLoggingEnabled(true);
        }
        return builder;
    }

    @Override
    protected void onApplicationConnected(ApplicationMetadata applicationMetadata, String applicationStatus, String sessionId, boolean wasLaunched) {

    }

    @Override
    protected void onApplicationConnectionFailed(int statusCode) {

    }

    @Override
    protected void onApplicationStopFailed(int statusCode) {

    }

    @Override
    protected void setDevice(CastDevice device) {
        Intent intent = new Intent(mContext, mActivityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                mContext, 0, intent, 0);

        CastRemoteDisplayLocalService.NotificationSettings settings =
                new CastRemoteDisplayLocalService.NotificationSettings.Builder()
                        .setNotificationPendingIntent(notificationPendingIntent).build();

        CastRemoteDisplayLocalService.startService(
                mContext,
                mCastRemoteDisplayLocalServiceClass, mCastConfiguration.getApplicationId(),
                device, settings,
                new CastRemoteDisplayLocalService.Callbacks() {
                    @Override
                    public void onServiceCreated(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
                        Log.w(TAG, "onServiceCreated");
                    }

                    @Override
                    public void onRemoteDisplaySessionStarted(
                            CastRemoteDisplayLocalService service) {
                        // initialize sender UI
                        Log.w(TAG, "onRemoteDisplaySessionStarted");
                    }

                    @Override
                    public void onRemoteDisplaySessionError(
                            Status errorReason){
                        initError();
                        String msg = "onRemoteDisplaySessionError: " + errorReason.toString();
                        Log.e(TAG, msg);
                    }

                    @Override
                    public void onRemoteDisplaySessionEnded(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
                        Log.w(TAG, "onRemoteDisplaySessionEnded");
                    }
                });
    }

    private void initError() {
        // TODO
    }

    @Override
    protected void onDeviceUnselected() {
        super.onDeviceUnselected();

        Log.w(TAG, "onDeviceUnselected");
        CastRemoteDisplayLocalService.stopService();
    }

    @Override
    public void launchApp(String applicationId, LaunchOptions launchOptions) throws TransientNetworkDisconnectionException, NoConnectionException {
        // Don't do anything
    }

    public boolean isRemoteDisplayRunning() {
        return (CastRemoteDisplayLocalService.getInstance() != null);
    }
}
