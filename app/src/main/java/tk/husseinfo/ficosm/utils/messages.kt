package tk.husseinfo.ficosm.utils

import android.content.Context
import android.net.Uri
import tk.husseinfo.ficosm.br.MC_SMS_SENDER_TOUCH
import tk.husseinfo.ficosm.models.MissedCall


fun getAllMessages(context: Context): List<MissedCall> {

    val messages = ArrayList<MissedCall>()

    val cursor = context.contentResolver?.query(Uri.parse("content://sms/inbox"), null, null, null, null);

    if (cursor!!.moveToFirst()) { // must check the result to prevent exception
        do {
            if (cursor.getString(2) != MC_SMS_SENDER_TOUCH)
                continue
            messages.add(getMissedCall(context, cursor.getString(12), cursor.getLong(4))!!)

        } while (cursor.moveToNext())
    } else {

    }

    cursor.close()
    return messages
}
