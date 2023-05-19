package com.example.apperger

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RompeCabezas : AppCompatActivity() {

    var mCurrentPhotoPath: String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rompe_cabezas)

        val am =assets
        try{
            val files= am.list("img")
            val grid = findViewById<GridView>(R.id.grid)
            grid.adapter = ImageAdapter(this@RompeCabezas)
            grid.onItemClickListener= AdapterView
                .OnItemClickListener { adapterView, view, i, l ->
                    val intent = Intent(applicationContext, CabezasActivity::class.java)
                    intent.putExtra("assetName",files!![i % files.size])
                    startActivity(intent)
                }
        }catch (e:IOException){
            e.printStackTrace()
            Toast.makeText(this@RompeCabezas,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

    }



    fun onImageCameraClicked (view:android.view.View){
        val intent = Intent (MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager)!=null){
            var photoFile: File? = null
            try{
                photoFile = createImageFile()
            }catch (e:IOException){
                e.printStackTrace()
                Toast.makeText(this@RompeCabezas,e.message,Toast.LENGTH_SHORT).show()
            }
            if(photoFile!=null){
                val photoUri = FileProvider.getUriForFile(
                    this@RompeCabezas,
                    applicationContext.packageName+".fileprovider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }
    fun onImageGalleryClicked(view: View) {
        if(ContextCompat.checkSelfPermission(
                this@RompeCabezas,android.Manifest.permission.READ_EXTERNAL_STORAGE
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this@RompeCabezas, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                ), REQUEST_PERMISSION_READ_EXTERNAL_STORAGE
            )
        }else  {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        if (ContextCompat.checkSelfPermission(
             this@RompeCabezas, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this@RompeCabezas, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        }
        else{
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmsss").format(Date())
            val imageFileName = "JPEG_"+timestamp+"_"
            val storageDir= Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            val image = File.createTempFile(
                imageFileName, ".jpg", storageDir
            )
            mCurrentPhotoPath= image.absolutePath
            return image
        }
        return null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode){
            REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE->{
                if(grantResults.size>0
                    && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    onImageCameraClicked(View(this@RompeCabezas))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val intent =Intent(
                this@RompeCabezas, CabezasActivity::class.java
            )
            intent.putExtra("mCurrentPhotoPath", mCurrentPhotoPath)
            startActivity(intent)
        }
        if(requestCode== REQUEST_IMAGE_GALLERY && resultCode== RESULT_OK){
            val uri = data!!.data
            intent.putExtra("mCurrentPhotoUri",uri)
            startActivity(intent)
        }
    }


    companion object{
      private const val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE =2
      private const val REQUEST_IMAGE_CAPTURE =1
      const val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE =3
      const val REQUEST_IMAGE_GALLERY =4
    }


}