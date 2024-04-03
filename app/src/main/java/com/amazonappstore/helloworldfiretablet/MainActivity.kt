package com.amazonappstore.helloworldfiretablet

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amazonappstore.helloworldfiretablet.ui.theme.HelloWorldFireTabletTheme
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class MainActivity : ComponentActivity() {

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
        val status: Int = googleApiAvailability.isGooglePlayServicesAvailable(this@MainActivity)
        return status == ConnectionResult.SUCCESS
    }

    private fun getInstallerPackageName(): String? {
        try {
            val packageName: String = packageName
            val pm: PackageManager = packageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val info = pm.getInstallSourceInfo(packageName)
                return info.installingPackageName
            }
            @Suppress("DEPRECATION") return pm.getInstallerPackageName(packageName)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("MainActivity", "Error $e")
        }
        return ""
    }

    private fun isInstalledFromAmazonAppstore(): Boolean {
        val installerPackageName = getInstallerPackageName()
        // If the app is installed from the Amazon Appstore
        // the installer package is equal to com.amazon.venezia
        return installerPackageName == getString(R.string.amazon_appstore_identifier)
    }

    private fun customTextToShow(isExpandedScreen: Boolean): String {
        return if (isExpandedScreen) resources.getString(R.string.tablet_expanded_screen)
        else resources.getString(R.string.tablet_not_expanded_screen)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isExpandedScreen =
                calculateWindowSizeClass(this).widthSizeClass == WindowWidthSizeClass.Expanded

            val textToShow = customTextToShow(isExpandedScreen)

            HelloWorldFireTabletTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        IdentifierImage(
                            isGooglePlayServicesAvailable(),
                            isInstalledFromAmazonAppstore(),
                            Modifier.align(
                                if (isExpandedScreen) {
                                    Alignment.CenterStart
                                } else {
                                    Alignment.TopStart
                                })
                        )
                        Text(
                            style = MaterialTheme.typography.headlineLarge,
                            text = textToShow,
                            modifier = Modifier
                                .align(
                                    if (isExpandedScreen) {
                                        Alignment.CenterEnd
                                    } else {
                                        Alignment.BottomCenter
                                    }
                                )
                                .padding(30.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IdentifierImage(
    isGooglePlayServicesAvailable: Boolean,
    installedFromAmazonAppstore: Boolean,
    modifier: Modifier
) {


    if (isGooglePlayServicesAvailable) {
        Image(
            painter = painterResource(id = R.drawable.younger_katsu_in_chair),
            contentDescription = stringResource(id = R.string.android_content_description),
            modifier = modifier
        )
    } else {
        if (installedFromAmazonAppstore) {
            Image(
                painter = painterResource(id = R.drawable.appstore_logo),
                contentDescription = stringResource(id = R.string.fos_content_description),
                modifier = modifier
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.older_katsu_in_chair),
                contentDescription = stringResource(id = R.string.other_content_description),
                modifier = modifier
            )
        }
    }
}