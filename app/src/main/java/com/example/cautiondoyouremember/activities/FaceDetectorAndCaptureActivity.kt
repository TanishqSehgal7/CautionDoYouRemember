package com.example.cautiondoyouremember.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.system.Os.read
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.loader.content.Loader
import com.camerakit.CameraKit
import com.camerakit.CameraKitView
import com.example.cautiondoyouremember.R
import com.example.cautiondoyouremember.databinding.ActivityFaceDetectorAndCaptureBinding
import dmax.dialog.SpotsDialog
import org.opencv.android.*
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FaceDetectorAndCaptureActivity() : AppCompatActivity(),
    CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var binding : ActivityFaceDetectorAndCaptureBinding
    private lateinit var waitingDialog : android.app.AlertDialog
    private val imageCapturedCount : Int = 0
//    private val totalImagesToBeCaptured : Int = 100
//    private val i=0
    lateinit var cascadeFile:File
    lateinit var faceDetector:CascadeClassifier
    private lateinit var mRGB : Mat
    private lateinit var mGrey : Mat

    val baseCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            super.onManagerConnected(status)
            when(status) {
                LoaderCallbackInterface.SUCCESS-> {
                    val inputStream:InputStream = resources.openRawResource(R.raw.haarcascade_frontalface_alt2)
                    val file: File = getDir("cascade", Context.MODE_PRIVATE)
                    cascadeFile = File(file,"haarcascade_frontalface_alt2")

                    val outputStream = FileOutputStream(cascadeFile)
                    val byteArray:ByteArray = ByteArray(4096)
                    val bytesRead=inputStream.read(byteArray)
                    while (bytesRead!=-1) {
                        outputStream.write(byteArray,0,bytesRead)
                    }
                    inputStream.close()
                    outputStream.close()
                    faceDetector= CascadeClassifier(cascadeFile.absolutePath)

                    if (faceDetector.empty()) {
                        faceDetector
                    } else {
                        file.delete()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceDetectorAndCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        System.loadLibrary("opencv_java3")
        OpenCVLoader.initDebug(true)

        val baseCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                super.onManagerConnected(status)
                when(status) {
                    LoaderCallbackInterface.SUCCESS-> {
                        val inputStream:InputStream = resources.openRawResource(R.raw.haarcascade_frontalface_alt2)
                        val file: File = getDir("cascade", Context.MODE_PRIVATE)
                        cascadeFile = File(file,"haarcascade_frontalface_alt2")

                        val outputStream = FileOutputStream(cascadeFile)
                        val byteArray:ByteArray = ByteArray(4096)
                        val bytesRead=inputStream.read(byteArray)
                        while (bytesRead!=-1) {
                            outputStream.write(byteArray,0,bytesRead)
                        }
                        inputStream.close()
                        outputStream.close()
                        faceDetector= CascadeClassifier(cascadeFile.absolutePath)

                        if (faceDetector.empty()) {
                            faceDetector
                        } else {
                            file.delete()
                        }
                    }
                }
            }
        }

        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0,this,baseCallback)
        } else {
            baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
            binding.camera.enableView()
        }

        binding.doThisLater.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.backBtnFaceCapture.setOnClickListener {
            finishAffinity()
        }

        binding.camera.setCvCameraViewListener(this)

        Handler().postDelayed(object :Runnable {
            override fun run() {

                waitingDialog = SpotsDialog.Builder().setContext(applicationContext)
                    .setMessage("Please make sure there is no other person in the camera frame")
                    .setCancelable(true)
                    .build().apply { show()
                    }
            }
        },3000)

        waitingDialog.cancel()
        waitingDialog.dismiss()

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

                        }
                    },2000)
                }

        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRGB = Mat()
        mGrey = Mat()
    }

    override fun onCameraViewStopped() {
        mRGB.release()
        mGrey.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        mRGB = inputFrame!!.rgba()
        val detections = MatOfRect()
        faceDetector.detectMultiScale(mRGB, detections)

        for (rect in detections.toArray()) {
            Imgproc.rectangle(mRGB, Point(rect.x.toDouble(), rect.y.toDouble()),Point((rect.x+rect.width).toDouble(),
                (rect.y+rect.height).toDouble()),Scalar(255.0, 0.0, 0.0))
        }
        return mRGB
    }
}