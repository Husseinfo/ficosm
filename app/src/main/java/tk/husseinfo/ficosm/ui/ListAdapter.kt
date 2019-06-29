package tk.husseinfo.ficosm.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tk.husseinfo.ficosm.R

import tk.husseinfo.ficosm.models.MissedCall

class MCListAdapter(private val dataset: List<MissedCall>) :
        RecyclerView.Adapter<MCListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView? = null
        private val mcDate: TextView? = null

        //        fun bind(item: MissedCall, listener: (MissedCall) -> Unit) = with(itemView) {
        fun bind(item: MissedCall) = with(itemView) {
            contactName?.text = item.contactName
            mcDate?.text = item.date.toString()
//            setOnClickListener { listener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mcView = LayoutInflater.from(parent.context)
                .inflate(R.layout.missed_call_item, parent, false) as TextView

        return ViewHolder(mcView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(dataset.get(position))

    override fun getItemCount() = dataset.size

}