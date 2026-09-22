package com.telynet.telynetusers.core.ui

import android.content.Context
import android.content.Intent
import android.net.Uri

fun launchDialer(
    context: Context,
    phoneNumber: String,
) {
    val intent =
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${phoneNumber.replace(Regex("[^0-9+]"), "")}")
        }
    context.startActivity(intent)
}

fun launchGoogleMaps(
    context: Context,
    address: String,
) {
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
    val mapIntent =
        Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }
    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://maps.google.com/?q=${Uri.encode(address)}"),
            ),
        )
    }
}
