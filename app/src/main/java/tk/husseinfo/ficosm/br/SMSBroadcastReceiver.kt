package tk.husseinfo.ficosm.br

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.persistence.room.Room
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import tk.husseinfo.ficosm.R
import tk.husseinfo.ficosm.db.AppDatabase
import tk.husseinfo.ficosm.db.DATABASE_NAME
import tk.husseinfo.ficosm.utils.getMissedCall


public const val MC_SMS_SENDER_TOUCH: String = "164"
private const val NOTIFICATIONS_CHANNEL_ID: String = "MC_SMS"

class SMSBroadcastReceiver : BroadcastReceiver() {

    private val db = null

    private fun getDatabase(context: Context): AppDatabase {
        return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries().build()
    }

    override fun onReceive(context: Context, intent: Intent?) {
        for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            if (smsMessage.originatingAddress == MC_SMS_SENDER_TOUCH) {
                val mc = getMissedCall(context, smsMessage.displayMessageBody) ?: return
                getDatabase(context).missedCallDao().insertOne(mc)
                val builder = NotificationCompat.Builder(context, NOTIFICATIONS_CHANNEL_ID)
                        .setSmallIcon(R.drawable.icons8_missed_call_24)
                        .setContentTitle("${mc.contactName ?: mc.contactNumber} at ${mc.time}")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .addAction(R.drawable.icons8_phone_filled_24, context.resources.getString(R.string.call_number),
                                PendingIntent.getActivity(context, 2, Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mc.contactNumber, null)), PendingIntent.FLAG_UPDATE_CURRENT))


                if (mc.contactName == null) {
                    val searchIntent = Intent.parseUri("http://truecaller.com/search/lb/${mc.contactNumber?.replace("+961", "")}", Intent.URI_INTENT_SCHEME)
                    builder.addAction(R.drawable.icons8_search_filled_24, context.resources.getString(R.string.search_number),
                            PendingIntent.getActivity(context, 1, searchIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                }

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        notificationManager.createNotificationChannel(NotificationChannel(NOTIFICATIONS_CHANNEL_ID, "MC SMS", NotificationManager.IMPORTANCE_HIGH))
                    }
                } catch (e: Exception) {

                }

                with(NotificationManagerCompat.from(context)) {
                    notify(builder.hashCode(), builder.build())
                }
            }
        }
    }
}

