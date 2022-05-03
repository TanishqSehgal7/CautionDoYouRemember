package com.example.cautiondoyouremember.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.camerakit.CameraKitView
import com.example.cautiondoyouremember.Utils.RectOverlay
import com.example.cautiondoyouremember.databinding.ActivityFaceDetectorAndCaptureBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
//import com.google.firebase.ml.vision.FirebaseVision
//import com.google.firebase.ml.vision.common.FirebaseVisionImage
//import com.google.firebase.ml.vision.face.FirebaseVisionFace
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
//import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import dmax.dialog.SpotsDialog
import org.opencv.android.*
import java.lang.Exception

class FaceDetectorAndCaptureActivity() : AppCompatActivity()
//    CameraBridgeViewBase.CvCameraViewListener2
 {

    private lateinit var binding : ActivityFaceDetectorAndCaptureBinding
    private lateinit var waitingDialog : android.app.AlertDialog
    private val imageCapturedCount : Int = 0
    private val totalImagesToBeCaptured : Int = 50
    private val i=0
//    lateinit var cascadeFile:File
//    lateinit var faceDetector:CascadeClassifier
//    private lateinit var mRGB : Mat
//    private lateinit var mGrey : Mat

//    val baseCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
//        override fun onManagerConnected(status: Int) {
//            super.onManagerConnected(status)
//            when(status) {
//                LoaderCallbackInterface.SUCCESS-> {
//                    val inputStream:InputStream = resources.openRawResource(R.raw.haarcascade_frontalface_alt2)
//                    val file: File = getDir("cascade", Context.MODE_PRIVATE)
//                    cascadeFile = File(file,"haarcascade_frontalface_alt2")
//
//                    val outputStream = FileOutputStream(cascadeFile)
//                    val byteArray:ByteArray = ByteArray(4096)
//                    val bytesRead=inputStream.read(byteArray)
//                    while (bytesRead!=-1) {
//                        outputStream.write(byteArray,0,bytesRead)
//                    }
//                    inputStream.close()
//                    outputStream.close()
//                    faceDetector= CascadeClassifier(cascadeFile.absolutePath)
//
//                    if (faceDetector.empty()) {
//                        faceDetector
//                    } else {
//                        file.delete()
//                    }
//                }
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceDetectorAndCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        System.loadLibrary("opencv_java3")
//        OpenCVLoader.initDebug(true)
//        if (!OpenCVLoader.initDebug()) {
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,baseCallback)
//        } else {
//            baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
//            binding.camera.enableView()
//        }

        binding.doThisLater.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.backBtnFaceCapture.setOnClickListener {
            finishAffinity()
        }

//        binding.camera.setCvCameraViewListener(this)

        Toast.makeText(this,"Click Start Captrure to proceed",Toast.LENGTH_SHORT).show()

        binding.startCapture.setOnClickListener {
//            binding.graphicOverlay.clear()
            waitingDialog = SpotsDialog.Builder().setContext(this)
                .setMessage("Initializing Image Capture...")
                .setCancelable(true)
                .build().apply { show()
                    Handler().postDelayed(object :Runnable {
                        override fun run() {
                            waitingDialog.setCancelable(true)
                            waitingDialog.cancel()
                            waitingDialog.dismiss()

                            // image to be captured x 50 times atleast
                            binding.camera.captureImage(object : CameraKitView.ImageCallback{
                                override fun onImage(cameraKitView: CameraKitView?, byteArray: ByteArray?) {
//                                    for (i:Int in 1..totalImagesToBeCaptured) {
//                                        // capture 100
//                                    }
//                                    val bitmap = cameraKitView?.cameraListener?.onOpened()
                                    var bitmap = byteArray?.let { it1 ->
                                        BitmapFactory.decodeByteArray(byteArray,0,
                                            it1.size)
                                    }
                                    if (cameraKitView != null) {
                                        bitmap = bitmap?.let { it1 -> Bitmap.createScaledBitmap(it1, cameraKitView.width,cameraKitView.height,false) }
                                    }
                                    if (bitmap != null) {
//                                        runFaceDetector(bitmap)
                                    }
                                }

                            })

                        }
                    },2000)
                }

        }
    }

    override fun onStart() {
        super.onStart()
        binding.camera.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.camera.onResume()
    }

    override fun onPause() {
        binding.camera.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.camera.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        binding.camera.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

//     fun runFaceDetector (bitmap: Bitmap) {
//         val image = FirebaseVisionImage.fromBitmap(bitmap)
//          val options = FirebaseVisionFaceDetectorOptions.Builder().build()
////         val realTimeOpts = FaceDetectorOptions.Builder()
////             .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
////             .build()
//
//         val detector: FirebaseVisionFaceDetector = FirebaseVision.getInstance()
//             .getVisionFaceDetector(options)
//
//         detector.detectInImage(image)
//             .addOnSuccessListener(object : OnSuccessListener<List<FirebaseVisionFace>>{
//                 override fun onSuccess(firebaseVisionFace: List<FirebaseVisionFace>?) {
//                    processFaceResult(firebaseVisionFace)
//                 }
//             }).addOnFailureListener(object :OnFailureListener{
//                 override fun onFailure(p0: Exception) {
//
//                 }
//
//             })
//     }

//     private fun processFaceResult(firebaseVisionFace: List<FirebaseVisionFace>?) {
//        val count=0
//         for (face in firebaseVisionFace!!) {
//             val bounds = face.boundingBox
//             val rect: RectOverlay = RectOverlay(binding.graphicOverlay,bounds)
//             binding.graphicOverlay.add(rect)
//
//         }
//     }

//    override fun onCameraViewStarted(width: Int, height: Int) {
//        mRGB = Mat()
//        mGrey = Mat()
//    }
//
//    override fun onCameraViewStopped() {
//        mRGB.release()
//        mGrey.release()
//    }
//
//    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
//        mRGB = inputFrame!!.rgba()
//        val detections = MatOfRect()
//        faceDetector.detectMultiScale(mRGB, detections)
//
//        for (rect in detections.toArray()) {
//            Imgproc.rectangle(mRGB, Point(rect.x.toDouble(), rect.y.toDouble()),Point((rect.x+rect.width).toDouble(),
//                (rect.y+rect.height).toDouble()),Scalar(255.0, 0.0, 0.0))
//        }
//        return mRGB
//    }
}