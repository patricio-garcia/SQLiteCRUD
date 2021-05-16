package cl.untoque.sqlitecrud

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cl.untoque.sqlitecrud.databinding.ActivityAddRecordBinding
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class AddRecordActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101

    private val IMAGE_PICK_CAMERA_CODE = 102
    private val IMAGE_PICK_STORAGE_CODE = 103

    private var actionBar: ActionBar? = null
    private var imageUri: Uri? = null
    private var name: String? = ""
    private var age: String? = ""
    private var phone: String? = ""

    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>

    private lateinit var binding: ActivityAddRecordBinding

    private lateinit var dbHelper: DatabaseHelper

    private lateinit var pImageView: ImageView
    private lateinit var pName : EditText
    private lateinit var pAge : EditText
    private lateinit var pPhone : EditText
    private lateinit var addRecordButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar
        actionBar!!.title = "Agregar Información"
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        dbHelper = DatabaseHelper(this)

        cameraPermission = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        storagePermission = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        pImageView = binding.imgPerson
        pName = binding.txtPersonName
        pAge = binding.txtPersonAge
        pPhone = binding.txtPersonPhone
        addRecordButton = binding.btnAdd

        pImageView.setOnClickListener {
            imagePickDialog()
        }

        addRecordButton.setOnClickListener {
            inputData()
        }

    }

    private fun imagePickDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Seleccionar imagen desde")
        builder.setItems(options) { dialog, which ->
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestCameraPermission()
                }
                else {
                    pickFromCamera()
                }
            }
            else if (which == 1) {
                if (!checkStoragePermission()) {
                    requestStoragePermission()
                }
                else {
                    pickFromGallery()
                }
            }
        }
        builder.create().show()
    }

    private fun inputData() {
        name = ""+pName.text.toString().trim()
        age = ""+pAge.text.toString().trim()
        phone = ""+pPhone.text.toString().trim()

        val timestamp = System.currentTimeMillis()
        val id = dbHelper.insertInfo(
            ""+imageUri,
            ""+name,
            ""+age,
            ""+phone,
            ""+timestamp,
            ""+timestamp
        )

        Toast.makeText(
            this,
            "Información agregada con éxito",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun checkCameraPermission(): Boolean {
        val resultC = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val resultS = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED


        return resultC && resultS
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE)
    }

    private fun pickFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Titulo de la Imagen")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Descripción de la Imagen")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        startActivityForResult(
            cameraIntent,
            IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE)
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_STORAGE_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera()
                    }
                    else {
                        Toast.makeText(
                            this,
                            "Se requiere permiso para la cámara y el almacenamiento externo",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                    if (storageAccepted) {
                       pickFromGallery()
                    }
                    else {
                        Toast.makeText(
                            this,
                            "Se requiere permiso para el almacenamiento externo",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_STORAGE_CODE) {
                CropImage.activity(data!!.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this)
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val resultUri = result.uri
                    imageUri = resultUri
                    pImageView.setImageURI(resultUri)
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Toast.makeText(
                        this,
                        "" + error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}