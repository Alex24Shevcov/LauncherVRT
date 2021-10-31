package com.example.testlauncher2

import android.content.SharedPreferences

val FAVORITE_APPS = "FAVORITE_APPS"
val SWIPE_APPS = "SWIPE_APPS"
val APPS = "APPS"
val SWIPE_LEFT = "SWIPE_LEFT"
val SWIPE_RIGHT = "SWIPE_RIGHT"

lateinit var PREF_FAVORITE_APPS: SharedPreferences
lateinit var PREF_SWIPE_APPS: SharedPreferences

fun sortedApps(array: List<AppBlock>?): List<AppBlock> {
    var hasFavoriteApp = false
    for (obj in array!!) {

        if (obj.isFavorite) {
            hasFavoriteApp = true
            break
        }
    }

    if (hasFavoriteApp) {
        val tmpMutArr = mutableListOf<AppBlock>()
        val secondArray = array.sortedBy { it.appName }
        for (i in secondArray)
            if (i.isFavorite)
                tmpMutArr.add(i)

        tmpMutArr.sortedBy { it.appName }
        for (i in secondArray)
            if (!i.isFavorite)
                tmpMutArr.add(i)

        return tmpMutArr
    }
    return array.sortedBy { it.appName }
}

fun isFavoriteApp(packageApp: String): Boolean{
    val tmpSet = PREF_FAVORITE_APPS.getStringSet(APPS, null)
    if (!tmpSet.isNullOrEmpty())
        return tmpSet.contains(packageApp)
    return false
}
