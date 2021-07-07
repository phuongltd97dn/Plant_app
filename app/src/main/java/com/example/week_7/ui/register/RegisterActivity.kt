package com.example.week_7.ui.register

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.week_7.R
import com.example.week_7.database.MyDatabase
import com.example.week_7.extensions.afterTextChanged
import com.example.week_7.models.User
import com.example.week_7.ui.home.HomeActivity
import com.example.week_7.ui.register.asynctasks.RegisterAsyncTask
import kotlinx.android.synthetic.main.activity_register.*
import java.io.InputStream

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerAsyncTask: RegisterAsyncTask
    private val sharedPrefFile = "com.example.week_7"
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sharedPref = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean("isFirstTime", true)
        val dbUser = MyDatabase.getInstance(this).myDAO.getUser()
        registerAsyncTask =
            RegisterAsyncTask(
                this,
                dbUser,
                isFirstTime
            )
        registerAsyncTask.execute()

        onPhotoClickedListener()
        onEditTextChanged()
        onNextButtonClickedListener()
    }

    override fun onPause() {
        super.onPause()
        val preferencesEditor: SharedPreferences.Editor = sharedPref.edit()
        preferencesEditor.putBoolean("isFirstTime", false)
        preferencesEditor.apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                CAPTURE_IMAGE_CODE -> {
                    val imageBitmap = data.extras?.get("data") as Bitmap
                    imgPhoto.setImageBitmap(imageBitmap)
                }

                PICK_IMAGE_CODE -> {
                    val imageUri: Uri? = data.data
                    val imageStream: InputStream? = imageUri?.let {
                        contentResolver.openInputStream(it)
                    }
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    imgPhoto.setImageBitmap(selectedImage)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent()
                } else {
                    Toast.makeText(
                        this,
                        "Permission Denied!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            REQUEST_READ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchPickImageFromGalleryIntent()
                } else {
                    Toast.makeText(
                        this,
                        "Permission Denied!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun onPhotoClickedListener() {
        imgPhoto.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Change photo: ")
                .setItems(arrayOf("Take photo", "Choose photo")) { _, which ->
                    when (which) {
                        0 -> {
                            if (checkAndRequestCameraPermission()) {
                                dispatchTakePictureIntent()
                            } else {
                                Toast.makeText(this, "Permission not Granted!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        1 -> {
                            if (checkAndRequestReadPermission()) {
                                dispatchPickImageFromGalleryIntent()
                            } else {
                                Toast.makeText(this, "Permission not Granted!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            builder.show()
        }
    }

    private fun checkAndRequestCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cameraPermission =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_CODE
                )
                return false
            }
        }
        return true
    }

    private fun checkAndRequestReadPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPermission = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (readPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_READ_CODE
                )
                return false
            }
        }
        return true
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_CODE)
    }

    private fun dispatchPickImageFromGalleryIntent() {
        val pickImageIntent = Intent(Intent.ACTION_PICK)
        pickImageIntent.type = "image/*"
        startActivityForResult(pickImageIntent, PICK_IMAGE_CODE)
    }

    private fun isButtonNextEnable(): Boolean {
        if (imgPhoto.drawable != null &&
            edtUserName.text.isNotEmpty() &&
            edtUniversity.text.isNotEmpty() &&
            edtHomeTown.text.isNotEmpty()
        ) {
            return true
        }
        return false
    }

    private fun onEditTextChanged() {
        edtUserName.afterTextChanged {
            if (isButtonNextEnable()) {
                btnNext.isEnabled = true
                btnNext.setBackgroundResource(R.drawable.bg_button_next_active)
            } else {
                btnNext.isEnabled = false
                btnNext.setBackgroundResource(R.drawable.bg_button_next_inactive)
            }
        }
        edtUniversity.afterTextChanged {
            if (isButtonNextEnable()) {
                btnNext.isEnabled = true
                btnNext.setBackgroundResource(R.drawable.bg_button_next_active)
            } else {
                btnNext.isEnabled = false
                btnNext.setBackgroundResource(R.drawable.bg_button_next_inactive)
            }
        }
        edtHomeTown.afterTextChanged {
            if (isButtonNextEnable()) {
                btnNext.isEnabled = true
                btnNext.setBackgroundResource(R.drawable.bg_button_next_active)
            } else {
                btnNext.isEnabled = false
                btnNext.setBackgroundResource(R.drawable.bg_button_next_inactive)
            }
        }
    }


    private fun onNextButtonClickedListener() {
        btnNext.setOnClickListener {
            val userName = edtUserName.text.toString()
            val university = edtUniversity.text.toString()
            val homeTown = edtHomeTown.text.toString()
            val avatar = imgPhoto.drawable.toBitmap()
            val user = User(
                userName = userName,
                university = university,
                homeTown = homeTown,
                avatar = avatar
            )

            MyDatabase.getInstance(this).myDAO.insertUser(user)

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        const val REQUEST_CAMERA_CODE = 313
        const val REQUEST_READ_CODE = 314
        const val CAPTURE_IMAGE_CODE = 315
        const val PICK_IMAGE_CODE = 316
    }
}