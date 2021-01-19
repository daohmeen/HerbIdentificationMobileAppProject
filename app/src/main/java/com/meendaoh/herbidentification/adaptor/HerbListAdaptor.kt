package com.meendaoh.herbidentification.adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.meendaoh.herbidentification.DetailActivity
import com.meendaoh.herbidentification.Herb
import com.meendaoh.herbidentification.databinding.HerbListBinding
import java.util.*
import kotlin.collections.ArrayList

class HerbListAdaptor(var herbListAll:ArrayList<Herb>,var context: Context) : RecyclerView.Adapter<HerbListAdaptor.ViewHolder>() , Filterable {
    var herbList = ArrayList<Herb>(herbListAll)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HerbListAdaptor.ViewHolder {
        val binding = HerbListBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: HerbListAdaptor.ViewHolder, position: Int) {
        val binding = holder.binding
        binding.imageviewHerb.setImageResource(herbList[position].image)
        binding.textviewTitle.text = herbList[position].title
        binding.linearlayoutList.setOnClickListener {
            val intent = Intent(context,DetailActivity::class.java)
            intent.putExtra("title",herbList[position].title)
            intent.putExtra("image",herbList[position].image)
            intent.putExtra("detail",herbList[position].detail)
            intent.putExtra("benefit",herbList[position].benefit)
            intent.putExtra("benefitDetail",herbList[position].benefitDetail)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = herbList.size
    inner class ViewHolder(view: View, val binding:HerbListBinding): RecyclerView.ViewHolder(view){}

    override fun getFilter(): Filter {
        return FilterAdaptor()
    }

    inner class FilterAdaptor: Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filterList = ArrayList<Herb>()
            if(p0.toString().isEmpty())
                filterList.addAll(herbListAll)
            else{
                for(herb in herbListAll) {
                    if( herb.title.toLowerCase(Locale.ROOT).startsWith(p0.toString().toLowerCase(
                            Locale.ROOT))
                    )
                    filterList.add(herb)
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filterList
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            herbList.clear()
            herbList.addAll(p1!!.values as Collection<Herb>)
            notifyDataSetChanged()
        }

    }
}