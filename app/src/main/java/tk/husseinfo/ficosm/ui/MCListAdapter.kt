package tk.husseinfo.ficosm.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import tk.husseinfo.ficosm.R
import tk.husseinfo.ficosm.models.MissedCall


class MCListAdapter(private val calls: List<MissedCall>) :
        RecyclerView.Adapter<MCListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView? = itemView.findViewById(R.id.mc_contact)
        private val mcDate: TextView? = itemView.findViewById(R.id.mc_date)

        fun bind(item: MissedCall): Unit = with(itemView) {
            contactName?.text = item.contactName ?: item.contactNumber?.replace("+961", "")
            mcDate?.text = "${item.time} - ${item.day}"

            setOnClickListener {
                if (!item.inContacts)
                    context.startActivity(Intent.parseUri("http://truecaller.com/search/lb/${item.contactNumber?.replace("+961", "")}", Intent.URI_INTENT_SCHEME))
            }

            setOnLongClickListener {
                val clipboard = getSystemService(context, ClipboardManager::class.java)
                val clip = ClipData.newPlainText("number", item.contactNumber)
                clipboard?.primaryClip = clip
                Toast.makeText(context, "Number copied", Toast.LENGTH_SHORT).show()
                return@setOnLongClickListener true
            }
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
