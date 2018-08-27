package com.wizag.taxi.rider.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.wizag.taxi.common.components.BaseActivity;
import com.wizag.taxi.common.events.BackgroundServiceStartedEvent;
import com.wizag.taxi.common.events.ConnectEvent;
import com.wizag.taxi.common.events.ConnectResultEvent;
import com.wizag.taxi.common.utils.MyPreferenceManager;
import com.wizag.taxi.rider.R;
import com.wizag.taxi.rider.services.RiderService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RiderBaseActivity extends BaseActivity {
    MyPreferenceManager SP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SP = MyPreferenceManager.getInstance(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isServiceRunning = isMyServiceRunning(RiderService.class);
        if (!isServiceRunning)
            startService(new Intent(this, RiderService.class));
    }
    @Subscribe
    public void onServiceStarted(BackgroundServiceStartedEvent event) {
        tryConnect();
    }
    public void tryConnect() {
        String token = SP.getString("rider_token", null);
        if (token != null && !token.isEmpty()) {
            eventBus.post(new ConnectEvent(token));
            if(connectionProgressDialog == null) {
                connectionProgressDialog = new MaterialDialog.Builder(this)
                        .title(getString(com.wizag.taxi.common.R.string.connection_dialog_title))
                        .content(R.string.event_reconnecting)
                        .progress(true, 0)
                        .cancelable(false)
                        .show();
            } else {
                connectionProgressDialog.setContent(R.string.event_reconnecting);
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectedResult(ConnectResultEvent event) {
        if(connectionProgressDialog != null)
            connectionProgressDialog.dismiss();
    }
}
