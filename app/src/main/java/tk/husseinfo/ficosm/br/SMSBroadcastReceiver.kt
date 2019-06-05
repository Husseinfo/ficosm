package tk.husseinfo.ficosm.br

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import tk.husseinfo.ficosm.R
import tk.husseinfo.ficosm.utils.Parser


private const val MC_SMS_SENDER_TOUCH: String = "164"
private const val NOTIFICATIONS_CHANNEL_ID: String = "MC_SMS"

class SMSBroadcastReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent?) {
        for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            if (smsMessage.originatingAddress == MC_SMS_SENDER_TOUCH) {
                val mc = Parser.getMissedCall(context, smsMessage.displayMessageBody)

                val builder = NotificationCompat.Builder(context, NOTIFICATIONS_CHANNEL_ID)
                        .setSmallIcon(R.drawable.icons8_missed_call_24)
                        .setContentTitle(context.resources.getString(R.string.missed_call_notification_title))
                        .setContentText("[${mc?.count}] ${mc?.contact ?: mc?.number} ${mc?.date}")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .addAction(R.drawable.icons8_phone_filled_24, context.resources.getString(R.string.call_number),
                                PendingIntent.getActivity(context, 2, Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mc?.number, null)), PendingIntent.FLAG_UPDATE_CURRENT))


                if (mc?.contact != null) {
                    val searchIntent = Intent.parseUri("http://truecaller.com/search/lb/${mc.number?.replace("+961", "")}", Intent.URI_INTENT_SCHEME)
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

