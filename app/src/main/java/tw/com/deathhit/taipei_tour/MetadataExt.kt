package tw.com.deathhit.taipei_tour

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

internal fun Context.getTravelTaipeiBaseUrl() =
    getMetadataString("tw.com.deathhit.taipei_tour.TRAVEL_TAIPEI_BASE_URL")

private fun Context.getMetadataString(key: String) = with(packageManager) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(
            packageName,
            PackageManager.PackageInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )
    } else {
        getPackageInfo(
            packageName,
            PackageManager.GET_META_DATA
        )
    }
}.applicationInfo.metaData.getString(key)!!