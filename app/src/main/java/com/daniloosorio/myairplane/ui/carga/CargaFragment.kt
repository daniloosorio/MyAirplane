package com.daniloosorio.myairplane.ui.carga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.daniloosorio.myairplane.R
import com.daniloosorio.myairplane.model.remote.CargaRemote
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_carga.*
import kotlinx.android.synthetic.main.fragment_list.*


class CargaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_carga, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_carga.visibility = GONE
        bt_delete.visibility = GONE
        bt_save.setOnClickListener {

            if(et_name.text.isNullOrEmpty()|| et_peso.text.isNullOrEmpty()){
                Toast.makeText(context, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show()
            }else if(et_peso.text.toString().toInt()<=500) {
                var name = et_name.text.toString()
                var weight = et_peso.text.toString().toInt()
                var price = 0
                    if (weight >= 0 && weight <= 25) {
                            price =0
                    } else if (weight >= 26 && weight <= 300) {
                            price = 1500*weight
                    } else if (weight >= 301 && weight <= 500){
                            price = 2500*weight
                    }

                    guardarEnFirebase(name, weight, price)
                    Toast.makeText(context, "Equipaje guardado}", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, "El peso no puede superar los 500kg", Toast.LENGTH_SHORT).show()
            }
        }

        bt_search.setOnClickListener {
            if(et_name2.text.isNullOrEmpty()){
                Toast.makeText(context, "Debe escribir un nombre", Toast.LENGTH_SHORT).show()
            }else {
                var name= et_name2.text.toString()
                leerEnFirebase(name)
            }
        }
        bt_delete.setOnClickListener {
            if(et_name2.text.isNullOrEmpty()){
                Toast.makeText(context, "Debe escribir un nombre", Toast.LENGTH_SHORT).show()
            }else {
                bt_search.visibility = VISIBLE
                bt_delete.visibility = GONE
                tv_carga.visibility = GONE
                tv_mensaje.visibility = VISIBLE
                et_name2.visibility = VISIBLE

                var name= et_name2.text.toString()
                borrarEnFirebase(name)
            }
        }


    }

    private fun borrarEnFirebase(name: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("equipajes")

        var nameExist =false
        val postListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (datasnapshot: DataSnapshot in snapshot.children){
                    val equipaje = datasnapshot.getValue(CargaRemote::class.java)
                    if(equipaje?.name==name){
                        myRef.child(equipaje.id!!).removeValue()
                        Toast.makeText(context, "equipaje retirado", Toast.LENGTH_SHORT).show()
                        nameExist= true
                    }
                }
                if(!nameExist){
                    Toast.makeText(context, "No se encuentra el usuario y su equipaje", Toast.LENGTH_SHORT).show()}

            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }

    private fun leerEnFirebase(name: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("equipajes")

        var nameExist =false
        val postListener = object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
             for (datasnapshot: DataSnapshot in snapshot.children){
                 val equipaje = datasnapshot.getValue(CargaRemote::class.java)
                    if(equipaje?.name==name){
                        tv_carga.text ="Nombre: ${equipaje.name} \n" +
                                "Peso: ${equipaje.weight} \n" +
                                "Valor: ${equipaje.price}"
                        bt_delete.visibility = VISIBLE
                        bt_search.visibility= GONE
                        tv_carga.visibility=VISIBLE
                        et_name2.visibility = GONE
                        tv_mensaje.visibility= GONE
                        nameExist= true
                    }
             }
                if(!nameExist){
                    Toast.makeText(context, "No se encuentra el usuario y su equipaje", Toast.LENGTH_SHORT).show()}

            }
        }
        myRef.addListenerForSingleValueEvent(postListener)
    }

    private fun guardarEnFirebase(name: String, weight: Int, price: Int) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("equipajes")
        val id = myRef.push().key
        val carga = CargaRemote(
           id,
            name,
            weight,
            price
        )
        myRef.child(id!!).setValue(carga)
    }
    
}