package com.daniloosorio.myairplane.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daniloosorio.myairplane.R
import com.daniloosorio.myairplane.model.remote.CargaRemote
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_carga.*
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {
    private val equipajeList :MutableList<CargaRemote> = mutableListOf()
    private lateinit var equipajeAdapter : EquipajeRVAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list, container, false)
         return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        leerEnFirebase()
        cargarEquipaje()
        rv_equipaje.layoutManager=LinearLayoutManager(
            requireContext(),RecyclerView.VERTICAL,
            false
        )
        rv_equipaje.setHasFixedSize(true)
        equipajeAdapter=
            EquipajeRVAdapter(
                equipajeList as ArrayList<CargaRemote>
            )
        rv_equipaje.adapter=equipajeAdapter


    }
    private fun leerEnFirebase() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("equipajes")
        var totalPeso=0
        var NumBultos=0
        var mayor =0
        var menor=500
        var promedio=0F
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children){
                    val equipaje = datasnapshot.getValue(CargaRemote::class.java)
                    totalPeso+=equipaje?.weight!!
                    NumBultos+=1
                    if(equipaje.weight>=mayor) mayor = equipaje.weight
                    if(equipaje.weight<=menor) menor = equipaje.weight
                }
                promedio = (totalPeso/NumBultos).toFloat()
                tv_data.text="Peso total del avion: $totalPeso \n " +
                        "Numero de bultos:$NumBultos \n" +
                        "Bulto mayor: $mayor \n" +
                        "Bulto menor: $menor \n " +
                        "Promedio de bultos $promedio"
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }

    private fun cargarEquipaje(){
        equipajeList.clear()
        val database =FirebaseDatabase.getInstance()
        val myRef = database.getReference("equipajes")
        val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(datasnapshot: DataSnapshot in snapshot.children){
                    val equipaje = datasnapshot.getValue(CargaRemote::class.java)
                    equipajeList.add(equipaje!!)
                }
                equipajeAdapter.notifyDataSetChanged()
            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }
}