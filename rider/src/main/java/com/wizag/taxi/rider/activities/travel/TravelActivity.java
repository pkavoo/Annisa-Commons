package com.wizag.taxi.rider.activities.travel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.wizag.taxi.common.activities.chargeAccount.ChargeAccountActivity;
import com.wizag.taxi.common.components.BaseActivity;
import com.wizag.taxi.common.components.LoadingDialog;
import com.wizag.taxi.common.events.ServiceCallRequestEvent;
import com.wizag.taxi.common.events.ServiceCallRequestResultEvent;
import com.wizag.taxi.common.events.ServiceCancelEvent;
import com.wizag.taxi.common.events.ServiceCancelResultEvent;
import com.wizag.taxi.common.location.MapHelper;
import com.wizag.taxi.common.models.Review;
import com.wizag.taxi.common.models.Travel;
import com.wizag.taxi.common.utils.AlertDialogBuilder;
import com.wizag.taxi.common.utils.AlerterHelper;
import com.wizag.taxi.common.utils.CommonUtils;
import com.wizag.taxi.common.utils.ServerResponse;
import com.wizag.taxi.rider.R;
import com.wizag.taxi.rider.activities.travel.adapters.TravelTabsViewPagerAdapter;
import com.wizag.taxi.rider.activities.travel.fragments.ReviewDialog;
import com.wizag.taxi.rider.activities.travel.fragments.TabStatisticsFragment;
import com.wizag.taxi.rider.databinding.ActivityTravelBinding;
import com.wizag.taxi.rider.events.ReviewDriverEvent;
import com.wizag.taxi.rider.events.ReviewDriverResultEvent;
import com.wizag.taxi.rider.events.ServiceFinishedEvent;
import com.wizag.taxi.rider.events.ServiceStartedEvent;
import com.transitionseverywhere.TransitionManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TravelActivity extends BaseActivity implements OnMapReadyCallback, ReviewDialog.onReviewFragmentInteractionListener, TabStatisticsFragment.onTravelInfoReceived {
    ActivityTravelBinding binding;
    Travel travel;
    Marker pickupMarker;
    Marker driverMarker;
    Marker destinationMarker;
    LatLng driverLocation;
    GoogleMap gMap;
    TravelTabsViewPagerAdapter travelTabsViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_travel);
        travel = Travel.fromJson(getIntent().getStringExtra("travel"));
        binding.slideCancel.setOnSlideCompleteListener(slideView -> eventBus.post(new ServiceCancelEvent()));
        binding.slideCall.setOnSlideCompleteListener(slideView -> TravelActivity.this.onCallDriverClicked(null));
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        travelTabsViewPagerAdapter = new TravelTabsViewPagerAdapter(getSupportFragmentManager(), TravelActivity.this, travel);
        binding.viewpager.setAdapter(travelTabsViewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
        if(travel.getRating() != null) {
            travelTabsViewPagerAdapter.deletePage(2);
            TabLayout.Tab tab = binding.tabLayout.getTabAt(0);
            if (tab != null)
                tab.select();
        }
        if(travel.getStartTimestamp() != null) {
            TransitionManager.beginDelayedTransition((ViewGroup) (binding.getRoot()));
            binding.slideCall.setVisibility(View.GONE);
            binding.slideCancel.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceFinished(ServiceFinishedEvent event) {
        String message;
        travel.setFinishTimestamp(new Timestamp(System.currentTimeMillis()));
        if (event.isCreditUsed)
            message = getString(R.string.travel_finished_taken_from_balance, event.amount);
        else
            message = getString(R.string.travel_finished_not_sufficient_balance, event.amount);
        new MaterialDialog.Builder(this)
                .title(R.string.message_default_title)
                .content(message)
                .positiveText(R.string.alert_ok)
                .onPositive((dialog, which) -> {
                    if (travelTabsViewPagerAdapter.getCount() == 2) {
                        finish();
                        return;
                    }
                    FragmentManager fm = getSupportFragmentManager();
                    ReviewDialog reviewDialog = ReviewDialog.newInstance();
                    reviewDialog.show(fm, "fragment_review_travel");
                }).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceCanceled(ServiceCancelResultEvent event) {
        if (event.hasError()) {
            event.showError(TravelActivity.this, result -> {
                if (result == AlertDialogBuilder.DialogResult.RETRY)
                    eventBus.post(new ServiceCancelEvent());
            });
            return;
        }
        AlertDialogBuilder.show(TravelActivity.this, getString(R.string.service_canceled), AlertDialogBuilder.DialogButton.OK, result -> finish());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReviewResult(ReviewDriverResultEvent event) {
        if (event.response == ServerResponse.OK) {
            if (travel.getFinishTimestamp() != null) {
                finish();
                return;
            }
            AlerterHelper.showInfo(TravelActivity.this, getString(R.string.message_review_sent));
            travelTabsViewPagerAdapter.deletePage(2);
            TabLayout.Tab tab = binding.tabLayout.getTabAt(0);
            if (tab != null)
                tab.select();

        } else
            event.showAlert(TravelActivity.this);
    }

    public void onChargeAccountClicked(View view) {
        Intent intent = new Intent(TravelActivity.this, ChargeAccountActivity.class);
        if (travel.getCostBest() - CommonUtils.rider.getBalance() > 0)
            intent.putExtra("defaultAmount", travel.getCostBest() - CommonUtils.rider.getBalance());
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTravelStarted(ServiceStartedEvent event) {
        travel.setStartTimestamp(new Timestamp(System.currentTimeMillis()));
        AlerterHelper.showInfo(TravelActivity.this, getString(R.string.message_travel_started));
        updateMarkers();
        TransitionManager.beginDelayedTransition((ViewGroup) (binding.getRoot()));
        binding.slideCall.setVisibility(View.GONE);
        binding.slideCancel.setVisibility(View.GONE);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setTrafficEnabled(true);
        updateMarkers();
    }

    void updateMarkers() {
        List<LatLng> locations = new ArrayList<>();
        if (pickupMarker == null)
            pickupMarker = gMap.addMarker(new MarkerOptions()
                    .position(travel.getPickupPoint())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pickup)));
        else
            pickupMarker.setPosition(travel.getPickupPoint());
        if (destinationMarker == null)
            destinationMarker = gMap.addMarker(new MarkerOptions()
                    .position(travel.getDestinationPoint())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_destination)));
        else
            destinationMarker.setPosition(travel.getDestinationPoint());
        if (driverLocation != null) {
            locations.add(driverLocation);
            if (travel.getStartTimestamp() != null)
                locations.add(travel.getDestinationPoint());
            else
                locations.add(travel.getPickupPoint());
            if (driverMarker == null)
                driverMarker = gMap.addMarker(new MarkerOptions()
                        .position(driverLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_taxi)));
            else
                driverMarker.setPosition(driverLocation);
        } else {
            locations.add(travel.getPickupPoint());
            locations.add(travel.getDestinationPoint());
        }

        MapHelper.centerLatLngsInMap(gMap, locations, true);

    }

    PermissionListener callPermissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:+" + travel.getDriver().getMobileNumber()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {

        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallRequested(ServiceCallRequestResultEvent event) {
        LoadingDialog.dismiss();
        if (event.hasError()) {
            event.showError(TravelActivity.this, result -> {
                if (result == AlertDialogBuilder.DialogResult.RETRY)
                    onCallDriverClicked(null);
            });
            return;
        }
        AlertDialogBuilder.show(TravelActivity.this, getString(R.string.call_request_sent));
    }

    @Override
    public void onReviewTravelClicked(Review review) {
        eventBus.post(new ReviewDriverEvent(review));
    }

    public void onCallDriverClicked(View view) {
        boolean isCallRequestEnabled = getResources().getBoolean(R.bool.is_call_request_enabled_rider);
        boolean isDirectCallEnabled = getResources().getBoolean(R.bool.is_direct_call_enabled_rider);
        if (isCallRequestEnabled && !isDirectCallEnabled)
            eventBus.post(new ServiceCallRequestEvent());
        if (!isCallRequestEnabled && isDirectCallEnabled)
            TedPermission.with(this)
                    .setPermissionListener(callPermissionListener)
                    .setDeniedMessage(R.string.message_permission_denied)
                    .setPermissions(Manifest.permission.CALL_PHONE)
                    .check();
        new MaterialDialog.Builder(this)
                .title(R.string.select_contact_approach)
                .items(new String[]{getString(R.string.direct_call), getString(R.string.operator_call)})
                .itemsCallback((dialog, view1, which, text) -> {
                    if (which == 0)
                        TedPermission.with(TravelActivity.this)
                                .setPermissionListener(callPermissionListener)
                                .setDeniedMessage(R.string.message_permission_denied)
                                .setPermissions(Manifest.permission.CALL_PHONE)
                                .check();
                    if (which == 1)
                        eventBus.post(new ServiceCallRequestEvent());
                })
                .show();
    }

    @Override
    public void onReceived(LatLng driverLocation, float cost) {
        this.driverLocation = driverLocation;
        updateMarkers();
    }
}
