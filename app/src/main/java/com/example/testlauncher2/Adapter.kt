package com.example.testlauncher2

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.testlauncher2.database.AppDatabaseDao
import kotlinx.android.synthetic.main.item_app.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch


// Адаптер context передал ему в качестве параметра,
// это сделано для упрощения задач, требующих параметра контекста.
class Adapter(
    private val context: Context,
) : RecyclerView.Adapter<Adapter.AppItemViewHolder>(){
    private var appList: List<AppBlock>? = null

    class AppItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

//    inner class AppItemViewHolder(
//        val appBinding: ItemAppBinding
//    ): RecyclerView.ViewHolder(appBinding.root)


    // В этом методе мы даём знать адаптеру, где наша "разукрашка",
    // где находится то, что мы будем показывать.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        //appBinding = ItemAppBinding.inflate(inflater, parent, false)
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
        // для запуска приложения
        holder.itemView.setOnClickListener {
            context.startActivity(
                context.packageManager.getLaunchIntentForPackage(
                    appList?.get(position)?.packageName ?: "com.krsolutions.yetanotherlauncher"
                )
            )
        }
        holder.itemView.switchFav.setOnClickListener {
            if (holder.itemView.switchFav.isChecked) {
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
                    "Удалено из в избранное ${appList?.get(position)?.appName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



    // Функция passAppList()используется для передачи a List<AppBlock>адаптеру.
    fun passAppList(appsList: List<AppBlock>?) {
        appList = appsList
        notifyDataSetChanged()
    }

    private fun addFavoriteApp(packageApp: String?){
        val tmpSet = PREF.getStringSet(APPS, mutableSetOf())
        tmpSet?.add(packageApp)
        val editor = PREF?.edit()
        editor?.putStringSet(APPS, tmpSet)
        editor?.apply()
        for (i in 0 until appList!!.size)
            if (appList!![i].packageName == packageApp && !appList!![i].isFavorite)
                appList!![i].isFavorite = true

    }

    private fun deleteFavoriteApp(packageApp: String?){
        val tmpSet = PREF.getStringSet(APPS, mutableSetOf())
        if (tmpSet.isNullOrEmpty())
            return
        tmpSet?.remove(packageApp)
        val editor = PREF?.edit()
        editor?.putStringSet(APPS, tmpSet)
        editor?.apply()
        for (i in 0 until appList!!.size)
            if (appList!![i].packageName == packageApp && appList!![i].isFavorite)
                appList!![i].isFavorite = false
    }


}