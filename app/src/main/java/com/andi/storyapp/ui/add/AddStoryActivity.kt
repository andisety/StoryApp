package com.andi.storyapp.ui.add


import android.Manifest
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.andi.storyapp.MainActivity
import com.andi.storyapp.MainActivity.Companion.CAMERA_X_RESULT
import com.andi.storyapp.R
import com.andi.storyapp.databinding.ActivityAddStoryBinding
import com.andi.storyapp.model.response.ApiResult
import com.andi.storyapp.preference.PreferenceLogin
import com.andi.storyapp.ui.camera.CameraActivity
import com.andi.storyapp.ui.viewmodel.StoryViewModel
import com.andi.storyapp.utils.reduceFileImage
import com.andi.storyapp.utils.rotateBitmap
import com.andi.storyapp.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddStoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var getFile: File? = null
    private  var isBackCamera:Boolean = false
    private  var isGalery:Boolean = false
    private lateinit var builder:AlertDialog.Builder
    private lateinit var dialog:AlertDialog
    private val storyViewModel:StoryViewModel by viewModels()
    private var lat:Float?=null
    private var lon:Float?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Add Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupDialogLoading()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()
        storyViewModel.responseUpload.observe(this){result->
            when(result){
                is ApiResult.Success->{
                    showDialogSuccesError(SUCCESS)
                    showDialogLoading(false)
                }
                is ApiResult.Error->{
                    showDialogSuccesError(ERROR)
                    showDialogLoading(false)
                }
                is ApiResult.Loading->{
                    showDialogLoading(true)
                }
            }
        }

        binding.btnCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }

        binding.btnUpload.setOnClickListener {
            if(binding.checkedLocation.isChecked){
                uploadImage(lat,lon)
            }else{
                uploadImage()
            }

        }

        binding.btnGaleri.setOnClickListener {
            startGallery()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        isGalery=false
    }

    private fun setupDialogLoading(){
        builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.custom_dialog_loading,null)
        builder.setTitle("Uploading Story")
        builder.setView(view)
         dialog = builder.create()
    }
    private fun showDialogLoading(isLoading:Boolean) {
        if (isLoading){
            dialog.show()
        }else{
            dialog.dismiss()
        }

    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            isGalery=false
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
            isGalery=true
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

    private fun uploadImage(latParam:Float?=null,lonParam:Float?=null) {
        if (getFile != null) {
            val desc = binding.etDesc.text.toString()
            if (desc.isEmpty()){
                binding.etDesc.error = "Masukan deskripsi"
                binding.etDesc.requestFocus()
            }else{
                val file = reduceFileImage(getFile as File,isBackCamera,isGalery)
                val description = desc.toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val prefLogin = PreferenceLogin(this)
                var token = prefLogin.getToken().toString()
                token = "Bearer $token"
                storyViewModel.uploadStory(token,imageMultipart,description,latParam,lonParam)
            }
        } else {
            Toast.makeText(this, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialogSuccesError(mode:String) {
        val builder = AlertDialog.Builder(this)
      if (mode== SUCCESS){
          val title = SUCCESS
          val message = "Upload berhasil"
          builder.setTitle(title)
          builder.setMessage(message)
          builder.setPositiveButton(android.R.string.ok) { _, _ ->
              val intent = Intent(this@AddStoryActivity,MainActivity::class.java)
              startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@AddStoryActivity).toBundle())
          }
          builder.show()
      }else if (mode== ERROR){
          val title = ERROR
          val message = "Upload Gagal, coba lagi"
          builder.setTitle(title)
          builder.setMessage(message)
          builder.setPositiveButton(android.R.string.ok) { _, _ ->

          }
          builder.show()
      }


    }




    private fun getMyLastLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat=location.latitude.toFloat()
                    lon=location.longitude.toFloat()
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    companion object{
        const val SUCCESS = "success"
        const val ERROR = "error"
    }


}