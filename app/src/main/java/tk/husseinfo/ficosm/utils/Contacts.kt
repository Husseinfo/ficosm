package tk.husseinfo.ficosm.utils

import android.content.Context
import android.net.Uri
import android.net.Uri.withAppendedPath
import android.provider.ContactsContract


class Contacts {

    companion object {
        private fun getContactFromPhoneBook(context: Context, number: String): String? {
            val uri = withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
            val cursor = context.contentResolver.query(uri,
                    arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
                    null,
                    null,
                    null)

            var name: String? = null

            try {
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToNext()
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
                return name
            }
        }

        fun getContact(context: Context, number: String): String? {
            return getContactFromPhoneBook(context, number)
        }
    }
}