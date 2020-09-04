package com.daniloosorio.myairplane.ui.tickets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.daniloosorio.myairplane.R
import kotlinx.android.synthetic.main.fragment_tickets.*

class TicketsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tickets, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_data.visibility=GONE
        bt_save.setOnClickListener {
            if (et_name.text.isNullOrEmpty() || et_distance.text.isNullOrEmpty() || et_time.text.isNullOrEmpty()) {
                Toast.makeText(context, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                tv_data.visibility= VISIBLE
                val name =et_name.text.toString()
                val distance= et_distance.text.toString().toFloat()
                val time= et_time.text.toString().toInt()
                var subtotal :Float = 0F
                var descuento :Float =0F
                subtotal = distance * 35.0F
                if (distance >= 1000 && time >= 7) {
                    descuento= subtotal*0.30F
                }
                var total = subtotal- descuento
                tv_data.text = "Pasajero: $name \n" +
                        "Distancia del viaje: $distance Km \n" +
                        "Duracion del viaje: $time Dias \n" +
                        "Subtotal tikete: $$subtotal \n" +
                        "Total descuento: $$descuento \n" +
                        "Total: $$total \n"
            }
        }
    }
}