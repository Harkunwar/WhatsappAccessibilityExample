package com.harkunwar.myapplication.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Notification
import android.util.Log
import android.app.PendingIntent.CanceledException




class NotificationReaderService : AccessibilityService() {
    
    val TAG = "WhatsappAccessExample"

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
                Log.d(TAG, "Recieved event")
                val data = event.parcelableData
                if (data is Notification) {
                    Log.d(TAG, "Recieved notification")
                    Log.d(TAG, "ticker: " + data.tickerText)
                    Log.d(TAG, "icon: " + data.iconLevel)
                    Log.d(TAG, "notification: " + event.text)
                    Log.d(TAG, "pkg name: "+event.packageName)
                    Log.d(TAG, "before text: "+event.beforeText)

                    try {
                        if(event.packageName == "com.whatsapp")
                            data.contentIntent.send()

                    } catch (e1: CanceledException) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace()
                    }

                }
            }
        }
    }

}