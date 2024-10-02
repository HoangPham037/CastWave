package com.castwave.castwave.utils

object Constants {
    const val KEY_SIGN_UP = "KEY_SIGN_UP"
    const val KEY_TYPE_OTP = "KEY_TYPE_OTP"
    const val KEY_SEND_OTP = "KEY_SEND_OTP"
    const val KEY_FORGOT_PASSWORD = "KEY_FORGOT_PASSWORD"
    const val KEY_PREFS_NAME = "data_store"
    const val DEFAULT_TIME_OUT = 60L
    const val KEY_JWT_TOKEN = "jwt_token"
    const val KEY_USER_INFO = "userInfo"
    const val KEY_TIMER = "KEY_TIMER"
    const val KEY_GUEST = "KEY_GUEST"
    const val KEY_GOOGLE_TOKEN = "KEY_GOOGLE_TOKEN"
    const val KEY_LINK_AUDIO = "https://firebasestorage.googleapis.com/v0/b/social-media-9d614.appspot.com/o/audio%2F123.mp3?alt=media&token=30576889-70a2-4d3b-958d-a7b0ebd570ed"

    //bottom nav
    const val INDEX_0 = 0
    const val INDEX_1 = 1
    const val INDEX_2 = 2
    const val INDEX_3 = 3
    const val INDEX_4 = 4
    const val INDEX_5 = 5
    const val INDEX_59 = 59

    //
    const val COLLAPSE_DEFAULT = 0
    const val COLLAPSE_POD_COURSE_CHAPTER = 3
    const val COLLAPSE_LINE_CONTENT = 5

    // key event bus
    const val KEY_FINISH_APP = "finishApp"
    const val VALUE_FINISH_APP = "finish"
    const val KEY_NAVIGATE_FRAGMENT = "navigateToFragment"
    const val VALUE_NAVIGATE_FRAGMENT = "toHome"
    const val KEY_BACK = "back"
    const val KEY_BACK_DISCOVERY = "back_discovery"
    const val VALUE_BACK_FROM_POD_COURSE = "backFromPodCourse"
    const val VALUE_BACK_FROM_SEARCH = "backFromSearch"
    const val VALUE_BACK_DISCOVERY = "backFromDiscovery"
    const val KEY_SHOW_SCREEN = "showScreen"
    const val VALUE_SHOW_SCREEN_PLAY_AUDIO = "playMusic"
    const val VALUE_SHOW_SCREEN_PLAY_VIDEO = "playVideo"
    const val KEY_HIDE_SCREEN_PLAY_VIDEO = "hideFragment"

    //bundle
    const val KEY_ADAPTER_TRENDING = "adapterTrending"
    const val VALUE_ADAPTER_TRENDING_1 = 1
    const val VALUE_ADAPTER_TRENDING_2 = 2

    const val KEY_ADAPTER_TOPIC = "adapterTopic"
    const val VALUE_ADAPTER_TOPIC_1 = 1
    const val VALUE_ADAPTER_TOPIC_2 = 2

}