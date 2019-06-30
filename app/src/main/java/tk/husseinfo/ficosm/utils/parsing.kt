package tk.husseinfo.ficosm.utils

import android.content.Context
import tk.husseinfo.ficosm.models.MissedCall
import java.text.SimpleDateFormat

fun getMissedCall(context: Context, sms: String): MissedCall? {
    try {
        val number: String = Regex("\\+\\d*").find(sms)?.value ?: return null
        val count: String? = Regex("\\[\\d{1,3}]").find(sms)?.value
        val day: String? = Regex("\\d{2}/\\d{2}/\\d{2,4}").find(sms)?.value
        val time: String? = Regex("\\d{2}:\\d{2}").find(sms)?.value
        val date: Long? = SimpleDateFormat("dd/MM/yy hh:mm").parse("$day $time").time
        val contact = getContact(context = context, number = number)
        return MissedCall(uid = null, contactName = contact, contactNumber = number, count = Integer.parseInt(count?.substring(1, 2)
                ?: "0"), date = date, inContacts = contact != null)
    } catch (e: Exception) {
        return null
    }
}
