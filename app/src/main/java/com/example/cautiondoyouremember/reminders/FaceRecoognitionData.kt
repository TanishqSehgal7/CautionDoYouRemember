package com.example.cautiondoyouremember.reminders

data class FaceRecoognitionData(
    val whoGotRecognized:String?="",
    val whenFaceGotRecognized:Long=0L,
    val notificationStatus: Boolean = false
) {

}
