package com.example.neighborsis.retrofit2.Constants

class PushConstants {

    companion object {

        const val SERVER_KEY : String = "AAAAT9nB8ek:APA91bF4BpGMjS938unYKnMaRJS3xFiL5pN_q4btufovBxKgSLsd_4o747OeI6kmfr59ake4881B4x9PNXbd3MYFlnRN0UhNUXmeCrLIdI1nmuTtTkY6_dAy5JAwPU_ckfa_-X9yc4ts"
        const val CONTENT_TYPE :String = "application/json"
        const val BASE_URL = "https://fcm.googleapis.com"

        //푸시 알림 수신자 체크를 위한 상태
        const val PUSH_SUBSCRIBED_ALL = "subscribedBoth"
        const val PUSH_SUBSCRIBED_SYSTEM = "subscribedSystem"
        const val PUSH_SUBSCRIBED_MARKETING = "subscribedMarketing"
        const val PUSH_SUBSCRIBED_NONE: String = "subscribedNone"
    }
}