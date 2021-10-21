package com.example.testlauncher2

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.testlauncher2.databinding.ItemAppBinding
import kotlinx.android.synthetic.main.item_app.view.*


// Адаптер context передал ему в качестве параметра,
// это сделано для упрощения задач, требующих параметра контекста.
class Adapter(
    val context: Context
) : RecyclerView.Adapter<Adapter.AppItemViewHolder>() {
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
                Toast.makeText(
                    context,
                    "Избранное ${appList?.get(position)?.packageName}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Не избранное ${appList?.get(position)?.packageName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Функция passAppList()используется для передачи a List<AppBlock>адаптеру.
    fun passAppList(appsList: List<AppBlock>) {
        appList = appsList
        notifyDataSetChanged()
    }

}