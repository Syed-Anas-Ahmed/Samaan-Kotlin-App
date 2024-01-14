package com.example.productsadder

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.productsadder.databinding.ActivityMainBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var seletedImages = mutableListOf<Uri>()
    private val selectedColors = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val h1 = findViewById<TextView>(R.id.h1)
        val h2 = findViewById<TextView>(R.id.h2)

        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (nightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Night mode (dark theme)
            h1.setTextColor(ContextCompat.getColor(this, R.color.white))
            h2.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            // Day mode (light theme)
            h1.setTextColor(ContextCompat.getColor(this, R.color.black))
            h2.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        binding.buttonColorPicker.setOnClickListener {
            ColorPickerDialog.Builder(this).setTitle("ColorPicker Dialog")
                .setPreferenceName("MyColorPickerDialog")
                .setPositiveButton("Select", object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.let {
                            selectedColors.add(it.color)
                            updateColors()
                        }
                    }
                }).setNegativeButton("Cancel") { colorPicker, _ ->
                    colorPicker.dismiss()
                }.show()
        }

        val selectImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data
                    //Multiple images selected

                    if (intent?.clipData != null) {
                        val count = intent.clipData?.itemCount ?: 0
                        (0 until count).forEach {
                            val imageUri = intent.clipData?.getItemAt(it)?.uri
                            imageUri?.let {
                                seletedImages.add(it)
                            }
                        }
                    } else {
                        //Single image selected
                        val imageUri = intent?.data
                        imageUri?.let {
                            seletedImages.add(it)
                        }
                    }
                    updateImages()
                }
            }

        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            selectImagesActivityResult.launch(intent)
        }
    }

    private fun updateImages() {
        binding.tvSelectedImages.setText(seletedImages.size.toString())
    }

    private fun updateColors() {
        var colorsStr = ""
        selectedColors.forEach {
            colorsStr = "$colorsStr ${Integer.toHexString(it)},"
        }
        binding.tvSelectedColors.text = colorsStr
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.saveProduct) {
            val productValidation = validateInformation()
            if (!productValidation) {
                Toast.makeText(this, "Check Your Inputs", Toast.LENGTH_LONG).show()
            } else {
                saveProduct()
                Toast.makeText(this, "Product Saved", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveProduct() {
        val name = binding.edName.text.toString().trim()
        val category = binding.edCategory.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val offerPercentage = binding.offerPercentage.text.toString().trim()
        val description = binding.edDescription.text.toString().trim()
        val colors = binding.tvSelectedColors.text.toString().trim()
        val sizes = getSizesList(binding.edSizes.text.toString().trim())
        val images = getImagesByteArrays()
    }

    private fun getImagesByteArrays(): List<ByteArray> {
        val imagesByteArrays = mutableListOf<ByteArray>()
        seletedImages.forEach {
            val stream = ByteArrayOutputStream()
            val imgBmp = MediaStore.Images.Media.getBitmap()
            byteArray?.let {
                imagesByteArrays.add(it)
            }
        }
        return imagesByteArrays

    }

    private fun getSizesList(sizesStr: String): List<String>? {
        if (sizesStr.isEmpty()) {
            return null
        }
        val sizesList = sizesStr.split(",")
        return sizesList
    }

    private fun validateInformation(): Boolean {
        if (binding.edPrice.text.toString().trim().isEmpty()) {
            return false
        } else if (binding.edName.text.toString().trim().isEmpty()) {
            return false
        } else if (binding.edCategory.text.toString().trim().isEmpty()) {
            return false
        } else if (seletedImages.isEmpty()) {
            return false
        }
        return true
    }
}