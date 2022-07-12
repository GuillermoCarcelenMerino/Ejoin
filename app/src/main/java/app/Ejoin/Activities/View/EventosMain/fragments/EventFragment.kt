package app.Ejoin.Activities.View.EventosMain.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.Model.Usuarios
import app.Ejoin.Adapter.RecyclerEventos
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.R

class EventFragment : Fragment() {
    private lateinit var eventos: ArrayList<EventoData>
    private lateinit var adapter : RecyclerEventos
    private lateinit var recyclerView : RecyclerView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var V=inflater.inflate(R.layout.fragment_event, container, false)
        recyclerView = V.findViewById(R.id.ReciclerEventoFragment)
        initRecycler()
        return V
    }

    companion object {

        @JvmStatic fun newInstance(param1: ArrayList<EventoData>) =
            EventFragment().apply {
                this.eventos=param1
            }
    }

    private fun initRecycler() {

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerEventos(requireActivity(),eventos)
        recyclerView.adapter = adapter
    }

    fun filtrarMapa( eventos : ArrayList<EventoData>){

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerEventos(requireActivity(), eventos)
        recyclerView.adapter = adapter
    }

}