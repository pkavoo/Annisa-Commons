package com.wizag.taxi.rider;

import org.greenrobot.eventbus.meta.SimpleSubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberMethodInfo;
import org.greenrobot.eventbus.meta.SubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;

import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

/** This class is generated by EventBus, do not edit. */
public class RiderEventBusIndex implements SubscriberInfoIndex {
    private static final Map<Class<?>, SubscriberInfo> SUBSCRIBER_INDEX;

    static {
        SUBSCRIBER_INDEX = new HashMap<Class<?>, SubscriberInfo>();

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.ui.RiderBaseActivity.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onServiceStarted",
                    com.wizag.taxi.common.events.BackgroundServiceStartedEvent.class),
            new SubscriberMethodInfo("onConnectedResult", com.wizag.taxi.common.events.ConnectResultEvent.class,
                    ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.activities.profile.ProfileActivity.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onProfileInfoChanged",
                    com.wizag.taxi.common.events.EditProfileInfoResultEvent.class, ThreadMode.MAIN),
            new SubscriberMethodInfo("onProfileImageChanged",
                    com.wizag.taxi.common.events.ChangeProfileImageResultEvent.class, ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.activities.main.dialogs.DriverAcceptedDialog.class,
                true, new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onServiceRequestResult",
                    com.wizag.taxi.rider.events.ServiceRequestResultEvent.class, ThreadMode.MAIN),
            new SubscriberMethodInfo("onDriverAccepted", com.wizag.taxi.rider.events.NewDriverAcceptedEvent.class,
                    ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.activities.splash.SplashActivity.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onLoginResultEvent", com.wizag.taxi.rider.events.LoginResultEvent.class,
                    ThreadMode.MAIN),
            new SubscriberMethodInfo("onConnectedResult", com.wizag.taxi.common.events.ConnectResultEvent.class,
                    ThreadMode.MAIN),
            new SubscriberMethodInfo("onServiceStarted",
                    com.wizag.taxi.common.events.BackgroundServiceStartedEvent.class),
        }));

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.activities.addresses.AddressesActivity.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onCRUDResultReceived", com.wizag.taxi.rider.events.CRUDAddressResultEvent.class,
                    ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.activities.travel.TravelActivity.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onServiceFinished", com.wizag.taxi.rider.events.ServiceFinishedEvent.class,
                    ThreadMode.MAIN),
            new SubscriberMethodInfo("onServiceCanceled", com.wizag.taxi.common.events.ServiceCancelResultEvent.class,
                    ThreadMode.MAIN),
            new SubscriberMethodInfo("onReviewResult", com.wizag.taxi.rider.events.ReviewDriverResultEvent.class,
                    ThreadMode.MAIN),
            new SubscriberMethodInfo("onTravelStarted", com.wizag.taxi.rider.events.ServiceStartedEvent.class,
                    ThreadMode.MAIN),
            new SubscriberMethodInfo("onCallRequested",
                    com.wizag.taxi.common.events.ServiceCallRequestResultEvent.class, ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.activities.main.MainActivity.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onAddressesReceived", com.wizag.taxi.rider.events.CRUDAddressResultEvent.class,
                    ThreadMode.MAIN),
            new SubscriberMethodInfo("onCalculateFareReceived",
                    com.wizag.taxi.rider.events.CalculateFareResultEvent.class, ThreadMode.MAIN),
            new SubscriberMethodInfo("onDriversLocationResult",
                    com.wizag.taxi.rider.events.GetDriversLocationResultEvent.class, ThreadMode.MAIN),
            new SubscriberMethodInfo("onRequestTaxiError", com.wizag.taxi.rider.events.ServiceRequestErrorEvent.class,
                    ThreadMode.MAIN),
            new SubscriberMethodInfo("onProfileChanged", com.wizag.taxi.common.events.ProfileInfoChangedEvent.class,
                    ThreadMode.MAIN, 0, true),
            new SubscriberMethodInfo("OnGetStatusResultReceived",
                    com.wizag.taxi.common.events.GetStatusResultEvent.class, ThreadMode.MAIN),
            new SubscriberMethodInfo("onAcceptDriver", com.wizag.taxi.rider.events.AcceptDriverUIEvent.class,
                    ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.activities.travel.fragments.TabStatisticsFragment.class,
                true, new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("onTravelInfoReceived",
                    com.wizag.taxi.rider.events.GetTravelInfoResultEvent.class, ThreadMode.MAIN),
        }));

        putIndex(new SimpleSubscriberInfo(com.wizag.taxi.rider.services.RiderService.class, true,
                new SubscriberMethodInfo[] {
            new SubscriberMethodInfo("connectSocket", com.wizag.taxi.common.events.ConnectEvent.class),
            new SubscriberMethodInfo("login", com.wizag.taxi.common.events.LoginEvent.class),
            new SubscriberMethodInfo("getStatus", com.wizag.taxi.common.events.GetStatusEvent.class),
            new SubscriberMethodInfo("EditProfile", com.wizag.taxi.common.events.EditProfileInfoEvent.class),
            new SubscriberMethodInfo("requestTaxi", com.wizag.taxi.rider.events.ServiceRequestEvent.class),
            new SubscriberMethodInfo("cancelTravel", com.wizag.taxi.common.events.ServiceCancelEvent.class),
            new SubscriberMethodInfo("acceptDriver", com.wizag.taxi.rider.events.AcceptDriverEvent.class),
            new SubscriberMethodInfo("getTravels", com.wizag.taxi.common.events.GetTravelsEvent.class),
            new SubscriberMethodInfo("getDriverLocation", com.wizag.taxi.rider.events.GetTravelInfoEvent.class),
            new SubscriberMethodInfo("reviewDriver", com.wizag.taxi.rider.events.ReviewDriverEvent.class),
            new SubscriberMethodInfo("ChangeProfileImage", com.wizag.taxi.common.events.ChangeProfileImageEvent.class),
            new SubscriberMethodInfo("getDriversLocation", com.wizag.taxi.rider.events.GetDriversLocationEvent.class),
            new SubscriberMethodInfo("WriteComplaint", com.wizag.taxi.common.events.WriteComplaintEvent.class),
            new SubscriberMethodInfo("chargeAccount", com.wizag.taxi.common.events.ChargeAccountEvent.class),
            new SubscriberMethodInfo("HideTravel", com.wizag.taxi.common.events.HideTravelEvent.class),
            new SubscriberMethodInfo("callRequest", com.wizag.taxi.common.events.ServiceCallRequestEvent.class),
            new SubscriberMethodInfo("onCalculateFareRequested",
                    com.wizag.taxi.rider.events.CalculateFareRequestEvent.class),
            new SubscriberMethodInfo("crudAddress", com.wizag.taxi.rider.events.CRUDAddressRequestEvent.class),
        }));

    }

    private static void putIndex(SubscriberInfo info) {
        SUBSCRIBER_INDEX.put(info.getSubscriberClass(), info);
    }

    @Override
    public SubscriberInfo getSubscriberInfo(Class<?> subscriberClass) {
        SubscriberInfo info = SUBSCRIBER_INDEX.get(subscriberClass);
        if (info != null) {
            return info;
        } else {
            return null;
        }
    }
}
