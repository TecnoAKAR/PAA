package com.example.apperger
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.apperger.databinding.ActivityPinturilloBinding
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.slider.RangeSlider


class Pinturillo : AppCompatActivity() {

    lateinit var binding: ActivityPinturilloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinturilloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startActivityForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    if (it.data != null && it.data!!.data != null) {
                        val bmp = binding.drawView.save()
                        val uri: Uri = it.data!!.data!!
                        val op = contentResolver.openOutputStream(uri)
                        bmp?.compress(Bitmap.CompressFormat.PNG, 100, op)
                    } else {
                        Toast.makeText(this, "Algun error ocurrió", Toast.LENGTH_SHORT).show()
                    }


                }
            }
        binding.btnUndo.setOnClickListener { binding.drawView.undo() }

        binding.btnSave.setOnClickListener {

            createFile("TecnoAkar.png", startActivityForResult)
        }
        binding.btnColor.setOnClickListener {


            ColorPickerDialog
                .Builder(this)
                .setTitle("Elige un color")
                .setColorListener { color, colorHex ->
                    binding.drawView.setColor(color)
                }
                .show()
        }
        binding.btnStroke.setOnClickListener {
            if (binding.rangebar.visibility == View.VISIBLE)
                binding.rangebar.visibility = View.GONE
            else binding.rangebar.visibility = View.VISIBLE
        }

        //set the range of the RangeSlider
        binding.rangebar.setValueFrom(0.0f)
        binding.rangebar.setValueTo(100.0f)
        //val grueso: Int
        //grueso=5000
        binding.rangebar.addOnChangeListener(RangeSlider.OnChangeListener { slider, value, fromUser ->
            binding.drawView.setStrokeWidth(
                value.toInt()
                //grueso
            )
        })

        val vto: ViewTreeObserver = binding.drawView.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.drawView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.drawView.measuredWidth
                val height = binding.drawView.measuredHeight
                binding.drawView.init(height, width)
            }
        })


    }

    fun createFile(fileName: String, launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TITLE, fileName)
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        )
        launcher.launch(intent)
    }
}