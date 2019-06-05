package tk.husseinfo.ficosm.br

import android.app.NotificationChannel
import android.app.NotificationManager
import android.arch.persistence.room.Room
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import tk.husseinfo.ficosm.R
import tk.husseinfo.ficosm.db.AppDatabase
import tk.husseinfo.ficosm.db.DATABASE_NAME
import tk.husseinfo.ficosm.utils.Parser


private const val MC_SMS_SENDER_TOUCH: String = "164"
private const val NOTIFICATIONS_CHANNEL_ID: String = "MC_SMS"

class SMSBroadcastReceiver : BroadcastReceiver() {

    private val db = null

    private fun getDatabase(context: Context): AppDatabase {
        return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java, DATABASE_NAME
        ).build()
    }

    override fun onReceive(context: Context, intent: Intent?) {
        for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            if (smsMessage.originatingAddress == MC_SMS_SENDER_TOUCH) {
                val mc = Parser.getMissedCall(context, smsMessage.displayMessageBody)
                if (mc != null) {
                    getDatabase(context).missedCallDao().insertOne(mc)
                    val builder = NotificationCompat.Builder(context, NOTIFICATIONS_CHANNEL_ID)
                            .setSmallIcon(R.drawable.icons8_missed_call_24)
                            .setContentTitle(context.resources.getString(R.string.missed_call_notification_title))
                            .setContentText("[${mc.count}] ${mc.contactName} ${mc.date}")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)

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
}

