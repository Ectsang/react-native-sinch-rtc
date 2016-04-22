var React = require('react-native');
var NativeModules = React.NativeModules;
var Platform = React.Platform;
var invariant = require('invariant');
// var SinchVerificationIOS = NativeModules.SinchVerificationIOS;
// var SinchVerificationAndroid = NativeModules.SinchVerificationAndroid;
var SinchRTC;


if (Platform.OS === 'ios') {
    // invariant(SinchVerificationIOS, 'Add SinchVerificationIOS.h and SinchVerificationIOS.m to your Xcode project');
    // SinchRTC = NativeModules.SinchAndroidRTCIOS;
    invariant(SinchRTC, "iOS is currently not supported.");
} else if (Platform.OS === 'android') {
    invariant(SinchRTC, 'Import libraries to android "rnpm link"');
    SinchRTC = NativeModules.SinchAndroidRTCAndroid;
} else {
    invariant(SinchRTC, "Invalid platform");
}

var applicationKey = null,
    applicationSecret = null,
    environmentHost = null,
    userId = null;

module.exports = {

	init: function(appKey, appSecret, envHost, uId) {
		applicationKey = appKey;
    applicationSecret = appSecret;
    environmentHost = envHost;
    userId = uId;

    SinchRTC.init(appKey, appSecret, envHost, uId);
	},

  startSinchClient: function(callback) {
    invariant(applicationKey, 'Call init() to setup the Sinch application key.');
    SinchRTC.startSinchClient(callback);
  },

  terminateSinchClient: function() {
    invariant(applicationKey, 'Call init() to setup the Sinch application key.');
    SinchRTC.terminateSinchClient();
  },

  setupAppToAppCall: function(remoteUserId) {
    invariant(applicationKey, 'Call init() to setup the Sinch application key.');
    SinchRTC.setupAppToAppCall(remoteUserId);
  },

  setupConferenceCall: function(conferenceId) {
    invariant(applicationKey, 'Call init() to setup the Sinch application key.');
    SinchRTC.setupConferenceCall(conferenceId);
  },

  answerIncomingCall: function(call) {
    invariant(applicationKey, 'Call init() to setup the Sinch application key.');
    SinchRTC.answerIncomingCall(call);
  },

  declineIncomingCall: function(call) {
    invariant(applicationKey, 'Call init() to setup the Sinch application key.');
    SinchRTC.declineIncomingCall(call);
  },

	// sms: function(phoneNumber, custom, callback) {
	// 	invariant(applicationKey, 'Call init() to setup the Sinch application key.');
	// 	SinchVerification.sms(applicationKey, phoneNumber, custom, callback);
	// },
  //
	// flashCall: function(phoneNumber, custom, callback) {
	// 	invariant(applicationKey, 'Call init() to setup the Sinch application key.');
	// 	SinchVerification.flashCall(applicationKey, phoneNumber, custom, callback);
	// },
  //
	// verify: SinchVerification.verify,

}
