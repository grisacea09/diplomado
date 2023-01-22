package com.intercam.folklore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intercam.folklore.databinding.ItemElementBinding
import com.intercam.folklore.model.vestimenta
import com.intercam.folklore.view.activities.MainActivity

class VestimentaAdapter(private val context: Context, private val vestimenta: ArrayList<vestimenta>): RecyclerView.Adapter<VestimentaAdapter.ViewHolder>() {

    class ViewHolder(view: ItemElementBinding): RecyclerView.ViewHolder(view.root) {
        val pais = view.tvPais
        val edo = view.tvEstado
        val etnia = view.tvEtnia
        val design = view.tvDesign
        val flag = view.tvbandera
        val img = view.ivescudo
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VestimentaAdapter.ViewHolder {
        val binding = ItemElementBinding.inflate(LayoutInflater.from(context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VestimentaAdapter.ViewHolder, position: Int) {
        holder.pais.text = "País: "+vestimenta[position].pais
        holder.edo.text = "Entidad: "+vestimenta[position].estado
        holder.etnia.text = "Etnia: "+vestimenta[position].etnia
        holder.design.text = "Diseñador: "+vestimenta[position].diseñador

        Glide.with(context)
            .load(vestimenta[position].flag)
            .circleCrop()
            .into(holder.flag)

        Glide.with(context)
            .load(vestimenta[position].escudo)
            .into(holder.img)

        holder.itemView.setOnClickListener{
            if(context is MainActivity) context.selectedVestimenta(vestimenta[position])
        }

    }

    override fun getItemCount(): Int {
        return vestimenta.size
    }
}