package com.andi.storyapp.ui.add

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.andi.storyapp.MainActivity
import com.andi.storyapp.MainActivity.Companion.CAMERA_X_RESULT
import com.andi.storyapp.databinding.ActivityAddStoryBinding
import com.andi.storyapp.model.response.ResponseStatus
import com.andi.storyapp.network.ApiConfig
import com.andi.storyapp.ui.camera.CameraActivity
import com.andi.storyapp.utils.rotateBitmap
import com.andi.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddStoryBinding
    private var getFile: File? = null
    private  var isBackCamera:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }

        binding.btnGaleri.setOnClickListener {
            startGallery()
        }



    }
    private fun reduceFileImage(file: File,isBackCamera:Boolean): File {
        var bitmap = BitmapFactory.decodeFile(file.path)
        bitmap = rotateBitmap(bitmap, isBackCamera)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
             isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.ivPreview.setImageBitmap(result)
        }
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.ivPreview.setImageURI(selectedImg)
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun showProgressBar(isLoading:Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun uploadImage() {
        showProgressBar(true)
        if (getFile != null) {

            val file = reduceFileImage(getFile as File,isBackCamera)

            val desc = binding.etDesc.text.toString()

            val description = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val service = ApiConfig.getApiService(this).uploadImage(imageMultipart,description)
            service.enqueue(object : Callback<ResponseStatus> {
                override fun onResponse(
                    call: Call<ResponseStatus>,
                    response: Response<ResponseStatus>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            showProgressBar(false)
                            Toast.makeText(this@AddStoryActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@AddStoryActivity,MainActivity::class.java))
                        }
                    } else {
                        showProgressBar(false)
                        Toast.makeText(this@AddStoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
                    showProgressBar(false)
                    Toast.makeText(this@AddStoryActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(this, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

}