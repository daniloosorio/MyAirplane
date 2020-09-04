package com.daniloosorio.myairplane.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniloosorio.myairplane.R
import com.daniloosorio.myairplane.model.remote.CargaRemote
import kotlinx.android.synthetic.main.item_equipaje.view.*

class EquipajeRVAdapter (

    var equipajeList: ArrayList<CargaRemote>
    ):RecyclerView.Adapter<EquipajeRVAdapter.EquipajeViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipajeViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_equipaje,parent,false)
            return EquipajeViewHolder(
                itemView
            )
        }

        override fun getItemCount(): Int = equipajeList.size

        override fun onBindViewHolder(holder: EquipajeViewHolder, position: Int) {
            val equipaje = equipajeList[position]
            holder.binEquipaje(equipaje)
        }

        class EquipajeViewHolder(
            itemView: View
        ): RecyclerView.ViewHolder(itemView){
            fun binEquipaje(equipaje:CargaRemote){
                itemView.tv_nombre.text="Nombre: ${equipaje.name}"
                itemView.tv_peso.text="Peso: ${equipaje.weight}"
                itemView.tv_precio.text="Pecio: ${equipaje.price}COP, ${equipaje.price/3700} USD"
            }
        }
}