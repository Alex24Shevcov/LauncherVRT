package com.example.testlauncher2

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_app.view.*


// Адаптер context передал ему в качестве параметра,
// это сделано для упрощения задач, требующих параметра контекста.
class Adapter(
    private val context: Context,
) : RecyclerView.Adapter<Adapter.AppItemViewHolder>() {
    private var appList: List<AppBlock>? = null

    // В этом методе мы даём знать адаптеру, где наша "разукрашка",
    // где находится то, что мы будем показывать.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appList?.size ?: 0
    }

    // В этом методе уже заполняем наш recyclerView.
    // Также можно отслеживать нажания на эл-ты recyclerView.
    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        holder.itemView.appIcon.setImageDrawable(appList?.get(position)?.icon)
        holder.itemView.appName.text = appList?.get(position)?.appName

        if (isFavoriteApp(appList?.get(position)?.packageName!!))
            holder.itemView.favoriteBtn.setImageResource(android.R.drawable.star_on)
        else
            holder.itemView.favoriteBtn.setImageResource(android.R.drawable.star_off)

    }


    // Функция passAppList()используется для передачи a List<AppBlock>адаптеру.
    @SuppressLint("NotifyDataSetChanged")
    fun passAppList(appsList: List<AppBlock>?) {
        appList = appsList
        notifyDataSetChanged()
    }

    @SuppressLint("MutatingSharedPrefs")
    private fun addFavoriteApp(packageApp: String?) {
        val tmpSet = PREF_FAVORITE_APPS.getStringSet(APPS, mutableSetOf())
        tmpSet?.add(packageApp)
        val editor = PREF_FAVORITE_APPS.edit()
        editor.clear()
        editor?.putStringSet(APPS, tmpSet)
        editor?.apply()
        for (i in 0 until appList!!.size)
            if (appList!![i].packageName == packageApp && !appList!![i].isFavorite)
                appList!![i].isFavorite = true
    }

    @SuppressLint("MutatingSharedPrefs")
    private fun deleteFavoriteApp(packageApp: String?) {
        val tmpSet = PREF_FAVORITE_APPS.getStringSet(APPS, mutableSetOf())
        if (tmpSet.isNullOrEmpty())
            return
        tmpSet.remove(packageApp)
        val editor = PREF_FAVORITE_APPS.edit()
        editor?.clear()
        editor?.putStringSet(APPS, tmpSet)
        editor?.apply()
        for (i in 0 until appList!!.size)
            if (appList!![i].packageName == packageApp && appList!![i].isFavorite)
                appList!![i].isFavorite = false
    }


    @SuppressLint("CommitPrefEdits")
    private fun addSwipeLeftApp(packageApp: String?) {
        val editor = PREF_SWIPE_APPS.edit()
        editor?.putString(SWIPE_LEFT, packageApp)
        editor?.apply()
    }

    @SuppressLint("CommitPrefEdits")
    private fun addSwipeRightApp(packageApp: String?) {
        val editor = PREF_SWIPE_APPS.edit()
        editor?.putString(SWIPE_RIGHT, packageApp)
        editor?.apply()
    }


    inner class AppItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            // для запуска приложения
            itemView.setOnClickListener {
                context.startActivity(
                    context.packageManager.getLaunchIntentForPackage(
                        appList?.get(position)?.packageName ?: "com.krsolutions.yetanotherlauncher"
                    )
                )
            }
            itemView.menuBtn.setOnClickListener {
                popupMenus(it)
            }
        }


        private fun popupMenus(v: View) {
            val popupMenus = PopupMenu(context, v)
            popupMenus.inflate(R.menu.popup_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favoriteItem -> {
                        if (!isFavoriteApp(appList?.get(position)?.packageName!!)) {
                            addFavoriteApp(appList?.get(position)?.packageName)

                            passAppList(sortedApps(appList))
                            Toast.makeText(
                                context,
                                "Добавлено в избранное ${appList?.get(position)?.appName}",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            deleteFavoriteApp(appList?.get(position)?.packageName)

                            passAppList(sortedApps(appList))


                            Toast.makeText(
                                context,
                                "Удалено из избранного ${appList?.get(position)?.appName}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }
                    R.id.swipe_Left_item -> {
                        addSwipeLeftApp(appList?.get(position)?.packageName)
                        Toast.makeText(
                            context,
                            "${appList?.get(position)?.appName} добавлено в левый свайп",
                            Toast.LENGTH_SHORT
                        ).show()

                        true
                    }

                    R.id.swipe_Right_item -> {
                        addSwipeRightApp(appList?.get(position)?.packageName)
                        Toast.makeText(
                            context,
                            "${appList?.get(position)?.appName} добавлено в правый свайп",
                            Toast.LENGTH_SHORT
                        ).show()

                        true
                    }

                    else -> true
                }
            }
            popupMenus.show()
        }
    }
}