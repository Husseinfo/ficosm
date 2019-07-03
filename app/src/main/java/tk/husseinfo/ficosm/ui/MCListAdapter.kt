package tk.husseinfo.ficosm.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tk.husseinfo.ficosm.R

import tk.husseinfo.ficosm.models.MissedCall

class MCListAdapter(private val calls: List<MissedCall>) :
        RecyclerView.Adapter<MCListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView? = itemView.findViewById(R.id.mc_contact)
        private val mcDate: TextView? = itemView.findViewById(R.id.mc_date)

        fun bind(item: MissedCall) = with(itemView) {
            contactName?.text = item.contactName ?: item.contactNumber
            mcDate?.text = "${item.time} - ${item.day}"
//            setOnClickListener { listener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mcView = LayoutInflater.from(parent.context)
                .inflate(R.layout.missed_call_item, parent, false)
        return ViewHolder(mcView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(calls[position])

    override fun getItemCount() = calls.size

}
