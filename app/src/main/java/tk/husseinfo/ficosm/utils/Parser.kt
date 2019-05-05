package tk.husseinfo.ficosm.utils

import android.content.Context
import tk.husseinfo.ficosm.models.MissedCall

class Parser {

    companion object {

        fun getMissedCall(context: Context, sms: String): MissedCall? {
            try {
                val number: String? = Regex("\\+\\d*").find(sms)?.value
                if (number == null)
                    return null
                val count: String? = Regex("\\[\\d{1,3}]").find(sms)?.value
                val date: String? = Regex("\\d{2}/\\d{2}/\\d{2,4}").find(sms)?.value + " at " +
                        Regex("\\d{2}:\\d{2}").find(sms)?.value
                val contact = Contacts.getContact(context = context, number = number)
                if(contact != null)
                    return MissedCall(contact = contact, count = Integer.parseInt(count?.substring(1,2)?:"0"), date = date)
                return null
            } catch (e: Exception) {
                return null
            }
        }
    }
}
