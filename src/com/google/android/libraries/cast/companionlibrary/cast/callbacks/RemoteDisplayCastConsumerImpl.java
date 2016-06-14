package com.google.android.libraries.cast.companionlibrary.cast.callbacks;

import android.support.v7.media.MediaRouter;

import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.libraries.cast.companionlibrary.cast.BaseCastManager;

/**
 * Created by benjamin on 16/01/16.
 */
public class RemoteDisplayCastConsumerImpl implements RemoteDisplayCastConsumer {
    @Override
    public void onConnected() {

    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onDisconnectionReason(@BaseCastManager.DisconnectReason int reason) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onCastDeviceDetected(MediaRouter.RouteInfo info) {

    }

    @Override
    public void onCastAvailabilityChanged(boolean castPresent) {

    }

    @Override
    public void onConnectivityRecovered() {

    }

    @Override
    public void onUiVisibilityChanged(boolean visible) {

    }

    @Override
    public void onReconnectionStatusChanged(int status) {

    }

    @Override
    public void onRouteRemoved(MediaRouter.RouteInfo info) {

    }

    @Override
    public void onDeviceSelected(CastDevice device, MediaRouter.RouteInfo routeInfo) {

    }

    @Override
    public void onFailed(int resourceId, int statusCode) {

    }
}
