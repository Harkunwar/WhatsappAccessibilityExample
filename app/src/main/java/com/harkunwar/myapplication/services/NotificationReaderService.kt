package com.harkunwar.myapplication.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.util.Log
import android.app.PendingIntent.CanceledException
import android.os.Build
import java.util.logging.Logger
import android.speech.tts.TextToSpeech
import java.util.*


class NotificationReaderService : AccessibilityService() {
    
    val TAG = "WhatsappAccessExample"
    lateinit var t1: TextToSpeech

    override fun onServiceConnected() {
        Log.d(TAG, "AccessibilityService Connected")
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        info.notificationTimeout = 100
        serviceInfo = info
    }


    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d(TAG, "FML")
        if (event != null) {
            @Suppress("DEPRECATED_IDENTITY_EQUALS")
            if (event.eventType === AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
                if(event.packageName.equals("com.whatsapp")) {
                    Log.d(TAG, "Recieved event")
                    val data = event.parcelableData
                    if (data is Notification) {
                        Log.d(TAG, "Recieved notification")
                        Log.d(TAG, "ticker: " + data.tickerText)
                        Log.d(TAG, "icon: " + data.iconLevel)
                        Log.d(TAG, "notification: " + event.text)
                        Log.d(TAG, "pkg name: " + event.packageName)
                        Log.d(TAG, "before text: " + event.beforeText)


                        var latestMessage = ""
                        val lines = data.extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES)
                        if (lines != null) {
                            latestMessage = lines[lines.size - 1] as String
                        } else {

    //                        val msgs = ArrayList<String>()
    //                        msgs.add("Info: " + data.extras.getString(Notification.EXTRA_INFO_TEXT))
    //                        msgs.add("Title: " + data.extras.getString(Notification.EXTRA_TITLE))
                            latestMessage = data.extras.getString(Notification.EXTRA_TEXT) as String
                        }

                        Log.d(TAG, "MESSAGE: $latestMessage")

                        t1 = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
                            if (status != TextToSpeech.ERROR) {
                                t1.language = Locale.UK
                                speakOut(latestMessage)
                            }
                        })
    //
    //
    //                    try {
    //                        if(event.packageName == "com.whatsapp")
    //                            data.contentIntent.send()
    //
    //
    //                    } catch (e1: CanceledException) {
    //                        e1.printStackTrace()
    //                    }
                    }
                }
            }
        }
    }

    private fun speakOut(message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            t1.speak(message, TextToSpeech.QUEUE_FLUSH, null,"")
        }
        else {
            t1.speak(message, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}