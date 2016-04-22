package com.ectsang.sinchandroid;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallListener;
import com.sinch.android.rtc.messaging.MessageClient;

import java.util.List;

/**
 * Created by eric on 4/22/16.
 */
public class SinchAndroidRTCModule extends ReactContextBaseJavaModule {

    private ReactApplicationContext mContext;
    private SinchClient sinchClient;
    private CallClient callClient;
    private MessageClient messageClient;
    private Callback mCallback;

    public SinchAndroidRTCModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return "SinchAndroidRTCAndroid";
    }

    @ReactMethod
    public void buildSinchClient(String applicationKey,
                                       String applicationSecret,
                                       String environmentHost,
                                       String userId,
                                       Callback callback) {

        mCallback = callback;
        sinchClient = Sinch.getSinchClientBuilder()
                .context(mContext)
                .applicationKey(applicationKey)
                .applicationSecret(applicationSecret)
                .environmentHost(environmentHost)
                .userId(userId)
                .build();

        sinchClient.setSupportMessaging(true);
        sinchClient.setSupportCalling(true);
        sinchClient.setSupportManagedPush(true);
    }

    @ReactMethod
    public void startSinchClient() {
        sinchClient.addSinchClientListener(new SinchClientListener() {
            public void onClientStarted(SinchClient client) {
            }

            public void onClientStopped(SinchClient client) {
            }

            public void onClientFailed(SinchClient client, SinchError error) {
            }

            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) {
            }

            public void onLogMessage(int level, String area, String message) {
            }
        });
        sinchClient.start();
    }

    @ReactMethod
    public void terminateSinchClient() {
        sinchClient.stopListeningOnActiveConnection();
        sinchClient.terminateGracefully();
    }

    @ReactMethod
    public void setupAppToAppCall(String remoteUserId) {
        callClient = sinchClient.getCallClient();
        Call call = callClient.callUser(remoteUserId);
        // Or for video call: Call call = callClient.callUserVideo("<remote user id>");
        call.addCallListener(new CallListener() {
            @Override
            public void onCallProgressing(Call call) {
                Log.i("CallProgressing", call.getCallId().toString());
            }

            @Override
            public void onCallEstablished(Call call) {
                Log.i("CallEstablished", call.getCallId().toString());
            }

            @Override
            public void onCallEnded(Call call) {
                Log.i("CallEnded", call.getCallId().toString());
            }

            @Override
            public void onShouldSendPushNotification(Call call, List<PushPair> list) {
                Log.i("ShouldSendPushNotif", call.getCallId().toString());
            }
        });
    }

    @ReactMethod
    public void setupConferenceCall(String conferenceId) {
        callClient = sinchClient.getCallClient();
        Call call = callClient.callConference(conferenceId);
        call.addCallListener(new CallListener() {
            @Override
            public void onCallProgressing(Call call) {
                Log.i("CallProgressing", call.getCallId().toString());
            }

            @Override
            public void onCallEstablished(Call call) {
                Log.i("CallEstablished", call.getCallId().toString());
            }

            @Override
            public void onCallEnded(Call call) {
                Log.i("CallEnded", call.getCallId().toString());
            }

            @Override
            public void onShouldSendPushNotification(Call call, List<PushPair> list) {
                Log.i("ShouldSendPushNotif", call.getCallId().toString());
            }
        });
    }


    @ReactMethod
    public void answerIncomingCall(Call call) {
        Log.i("answerIncomingCall", call.getCallId().toString());

        // User answers the call
        call.answer();
        // Stop playing ring tone
        // ...
    }

    @ReactMethod
    public void declineIncomingCall(Call call) {
        Log.i("declineIncomingCall", call.getCallId().toString());

        // User does not want to answer
        call.hangup();
        // Stop playing ring tone
        // ...
    }


    private void consumeCallback(Boolean success, WritableMap payload) {
        if (mCallback != null) {
            if (success) {
                mCallback.invoke(null, payload);
            } else {
                mCallback.invoke(payload, null);
            }
            mCallback = null;
        }
    }

}
