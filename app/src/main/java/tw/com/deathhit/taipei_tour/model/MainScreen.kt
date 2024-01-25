package tw.com.deathhit.taipei_tour.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface MainScreen : Parcelable {
    @Parcelize
    data class AttractionDetail(val attractionId: String) : MainScreen

    @Parcelize
    data object AttractionList : MainScreen

    @Parcelize
    data class Gallery(val attractionId: String) : MainScreen

    @Parcelize
    data class ImageViewer(val imageUrl: String) : MainScreen
}