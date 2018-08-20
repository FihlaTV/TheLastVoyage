package com.alter.thelastvoyage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alter.thelastvoyage.ui.main.MainFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {

    companion object {
        /** Log Tag.  */
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var interstitialAd: InterstitialAd
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var firebaseInstanceId: FirebaseInstanceId
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        firebaseInstanceId = FirebaseInstanceId.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        firebaseInstanceId.instanceId.addOnSuccessListener(this) { instanceIdResult ->
            val newToken = instanceIdResult.token
            Log.e("newToken", newToken)
        }

        MobileAds.initialize(this, "ca-app-pub-3760268662196714~1576198290")

        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        interstitialAd.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        }
        interstitialAd.loadAd(AdRequest.Builder().build())
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = firebaseAuth.currentUser
        Log.e(TAG, "Current user: " + currentUser.toString())
    }

    private fun onInviteClicked() {
        val intent = AppInviteInvitation.IntentBuilder("Invitation title")
                .setMessage("Invitation message")
//                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
//                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText("Invitation CTA")
                .build()
//        startActivityForResult(intent, REQUEST_INVITE)
    }
}
