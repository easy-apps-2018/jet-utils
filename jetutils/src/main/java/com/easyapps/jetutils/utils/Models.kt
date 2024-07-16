package com.easyapps.jetutils.utils

import android.os.*
import androidx.annotation.*
import kotlinx.parcelize.*

@Parcelize
data class NavItem(
    val mode: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val visible: Boolean = true,
    val selected: Boolean = false
) : Parcelable