package com.example.testlauncher2

import android.content.SharedPreferences

val FAVORITE_APPS = "FAVORITE_APPS"
val APPS = "APPS"
lateinit var PREF: SharedPreferences

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
    val tmpSet = PREF.getStringSet(APPS, null)
    if (!tmpSet.isNullOrEmpty())
        return tmpSet.contains(packageApp)
    return false
}
