package com.baokiin.mymusic.sns

class AppData {
    var idToken: String? = ""
    var token: String = ""
    var source: String? = null

    companion object {
        private var instance: AppData? = null
        fun g(): AppData {
            if (instance == null)
                instance = AppData()
            return instance!!
        }
    }
}
