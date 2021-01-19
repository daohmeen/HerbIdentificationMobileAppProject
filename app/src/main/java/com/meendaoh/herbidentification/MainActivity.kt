package com.meendaoh.herbidentification

import android.Manifest
import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.meendaoh.herbidentification.adaptor.HerbListAdaptor
import com.meendaoh.herbidentification.databinding.ActivityMainBinding
import com.pixplicity.easyprefs.library.Prefs

private const val CAMERA_CODE = 999
private const val GALLERY_CODE = 1000

private const val THAI_LANGUAGE_KEY = "isThai"

class MainActivity : AppCompatActivity() {
    private lateinit var herbAdaptor: HerbListAdaptor
    private lateinit var listOfHerbsAll: ArrayList<Herb>
    private lateinit var binding: ActivityMainBinding

    private val mInputSize = 224
    private val mModelPartPath = "modelPart.tflite"
    private val mModelFruitPath = "modelFruit.tflite"
    private val mModelRootPath = "modelRoot.tflite"
    private val mModelLeafPath = "modelLeaf.tflite"
    private val mLabelPartPath = "labelPart.txt"
    private val mLabelFruitPath = "labelFruit.txt"
    private val mLabelRootPath = "labelRoot.txt"
    private val mLabelLeafPath = "labelLeaf.txt"
    private lateinit var classifierPart: Classifier
    private lateinit var classifierRoot: Classifier
    private lateinit var classifierFruit: Classifier
    private lateinit var classifierLeaf: Classifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        listOfHerbsAll = ArrayList<Herb>()

        classifierPart = Classifier(assets, mModelPartPath, mLabelPartPath, mInputSize)
        classifierFruit = Classifier(assets, mModelFruitPath, mLabelFruitPath, mInputSize)
        classifierRoot = Classifier(assets, mModelRootPath, mLabelRootPath, mInputSize)
        classifierLeaf = Classifier(assets, mModelLeafPath, mLabelLeafPath, mInputSize)

        if(Prefs.getBoolean(THAI_LANGUAGE_KEY, false)){
            setupListOfHerbsAllTH()
            binding.buttonBar.text = getString(R.string.ENG)
            binding.textviewCameraButton.text = getString(R.string.button_text_cameraTH)
            binding.textviewGalleryButton.text = getString(R.string.button_text_galleryTH)
        }
        else {
            setupListOfHerbsAllEng()
        }

        setupAppBar()
        setupRecycleView()
        setupEditTextSearch()
        setupButton()

    }

    private fun setupListOfHerbsAllTH() {
        listOfHerbsAll.clear()
        val benefit = getString(R.string.benefit_herbTH)
        listOfHerbsAll.add(Herb(getString(R.string.balsamPearTH), R.drawable.balsampear
            ,getString(R.string.balsamPearTH_detail),benefit,getString(R.string.balsamPearTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.curcumaTH), R.drawable.curcuma
            ,getString(R.string.curcumaTH_detail),benefit,getString(R.string.curcumaTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.fingerrootTH), R.drawable.fingerroot
            ,getString(R.string.fingerrootTH_detail),benefit,getString(R.string.fingerrootTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.limeTH), R.drawable.lime
            ,getString(R.string.limeTH_detail),benefit,getString(R.string.limeTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.sesbaniaGrandifloraTH), R.drawable.sesbaniagrandiflora
            ,getString(R.string.sesbaniaGrandifioraTH_detail),benefit,getString(R.string.sesbeniaGrandifioraTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.tamarindTH), R.drawable.tamarind
            ,getString(R.string.tamarindTH_detail),benefit,getString(R.string.tamarindTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.thaiEggplantTH), R.drawable.thaieggplant
            ,getString(R.string.thaiEggplantTH_detail),benefit,getString(R.string.thaiEggplantTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.turkeyBerryTH), R.drawable.turkeyberry
            ,getString(R.string.turkeyBerryTH_detail),benefit,getString(R.string.turkeyBerryTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.kaffirLimeTH), R.drawable.kaffirlime
            ,getString(R.string.kaffirLimeTH_detail),benefit,getString(R.string.kaffirLimeTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.lemonGrassTH), R.drawable.lemongrass
            ,getString(R.string.lemonGrassTH_detail),benefit,getString(R.string.lemonGrassTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.eggplantTH), R.drawable.eggplant
            ,getString(R.string.eggplantTH_detail),benefit,getString(R.string.eggplantTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.gotuKolaTH), R.drawable.gotukola
            ,getString(R.string.gotuKolaTH_detail),benefit,getString(R.string.gotuKolaTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.sweetBasilTH), R.drawable.sweetbasil
            ,getString(R.string.sweetBasilTH_detail),benefit,getString(R.string.sweetBasilTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.holyBasilTH), R.drawable.holybasil
            ,getString(R.string.holyBasilTH_detail),benefit,getString(R.string.holyBasilTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.okraTH), R.drawable.okra
            ,getString(R.string.okraTH_detail),benefit,getString(R.string.okraTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.starFruitTH), R.drawable.starfruit
            ,getString(R.string.starFruitTH_detail),benefit,getString(R.string.starFruitTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.gingerTH), R.drawable.ginger
            ,getString(R.string.gingerTH_detail),benefit,getString(R.string.gingerTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.galangaTH), R.drawable.galanga
            ,getString(R.string.galangaTH_detail),benefit,getString(R.string.galangaTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.pepperTH), R.drawable.pepper
            ,getString(R.string.pepperTH_detail),benefit,getString(R.string.pepperTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.amlaTH), R.drawable.amla
            ,getString(R.string.amlaTH_detail),benefit,getString(R.string.amlaTH_benefit)))
        listOfHerbsAll.add(Herb(getString(R.string.pandanTH), R.drawable.pandan
            ,getString(R.string.pandanTH_detail),benefit,getString(R.string.pandanTH_benefit)))
        listOfHerbsAll.sortBy {it.title}
    }

    private fun setupListOfHerbsAllEng() {
        listOfHerbsAll.clear()
        val benefit = getString(R.string.benefit_herbENG)
        listOfHerbsAll.add(Herb(getString(R.string.balsamPearENG), R.drawable.balsampear))
        listOfHerbsAll.add(Herb(getString(R.string.curcumaENG), R.drawable.curcuma))
        listOfHerbsAll.add(Herb(getString(R.string.fingerrootENG), R.drawable.fingerroot))
        listOfHerbsAll.add(Herb(getString(R.string.limeENG), R.drawable.lime))
        listOfHerbsAll.add(Herb(getString(R.string.sesbaniaGrandifloraENG), R.drawable.sesbaniagrandiflora))
        listOfHerbsAll.add(Herb(getString(R.string.tamarindENG), R.drawable.tamarind))
        listOfHerbsAll.add(Herb(getString(R.string.thaiEggplantENG), R.drawable.thaieggplant))
        listOfHerbsAll.add(Herb(getString(R.string.turkeyBerryENG), R.drawable.turkeyberry))
        listOfHerbsAll.add(Herb(getString(R.string.kaffirLimeENG), R.drawable.kaffirlime))
        listOfHerbsAll.add(Herb(getString(R.string.lemonGrassENG), R.drawable.lemongrass))
        listOfHerbsAll.add(Herb(getString(R.string.eggplantENG), R.drawable.eggplant))
        listOfHerbsAll.add(Herb(getString(R.string.gotuKolaENG), R.drawable.gotukola))
        listOfHerbsAll.add(Herb(getString(R.string.sweetBasilENG), R.drawable.sweetbasil))
        listOfHerbsAll.add(Herb(getString(R.string.holyBasilENG), R.drawable.holybasil))
        listOfHerbsAll.add(Herb(getString(R.string.okraENG), R.drawable.okra))
        listOfHerbsAll.add(Herb(getString(R.string.starFruitENG), R.drawable.starfruit))
        listOfHerbsAll.add(Herb(getString(R.string.gingerENG), R.drawable.ginger))
        listOfHerbsAll.add(Herb(getString(R.string.galangaENG), R.drawable.galanga))
        listOfHerbsAll.add(Herb(getString(R.string.pepperENG), R.drawable.pepper))
        listOfHerbsAll.add(Herb(getString(R.string.amlaENG), R.drawable.amla))
        listOfHerbsAll.add(Herb(getString(R.string.pandanENG), R.drawable.pandan))
        listOfHerbsAll.sortBy {it.title}
    }

    private fun setupButton() {
        binding.buttonCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(this.packageManager)!=null)
                startActivityForResult(intent, CAMERA_CODE)
            else
                Toast.makeText(this, "can not open camera", Toast.LENGTH_SHORT).show()
        }
        binding.buttonGallery.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions, GALLERY_CODE);
                }
                else{
                    pickImageFromGallery();
                }
            }
            else{
                pickImageFromGallery();
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            GALLERY_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            modelPredict(takenImage)
        }
        else if(requestCode == GALLERY_CODE && resultCode == Activity.RESULT_OK){
            val inputBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
            modelPredict(inputBitmap)
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun modelPredict(inputBitmap: Bitmap){
        val resultPartPredict = classifierPart.recognizeImage(inputBitmap)
        val resultPredict = when(resultPartPredict.get(0).title){
            "Fruit" -> classifierFruit.recognizeImage(inputBitmap)
            "Root" -> classifierRoot.recognizeImage(inputBitmap)
            else -> classifierLeaf.recognizeImage(inputBitmap)
        }
        val predictTitle:String = matchLabel(resultPredict.get(0).title)
        Toast.makeText(
                applicationContext,
                "${predictTitle} ${resultPredict.get(0).confidence} ${resultPartPredict.get(0)}", Toast.LENGTH_LONG
        ).show()
        for(i in listOfHerbsAll){
            if(i.title == predictTitle) {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("title", i.title)
                intent.putExtra("image", i.image)
                intent.putExtra("detail",i.detail)
                intent.putExtra("benefit",i.benefit)
                intent.putExtra("benefitDetail",i.benefitDetail)
                startActivity(intent)
            }
        }
    }

    private fun matchLabel(title: String): String {
        val newTitle:String
        if (Prefs.getBoolean(THAI_LANGUAGE_KEY, false)){
            newTitle = when(title){
                getString(R.string.balsamPearLabel) -> getString(R.string.balsamPearTH)
                getString(R.string.curcumaLabel) -> getString(R.string.curcumaTH)
                getString(R.string.fingerrootLabel) -> getString(R.string.fingerrootTH)
                getString(R.string.limeLabel) -> getString(R.string.limeTH)
                getString(R.string.sesbaniaGrandifloraLabel) -> getString(R.string.sesbaniaGrandifloraTH)
                getString(R.string.tamarindLabel) -> getString(R.string.tamarindTH)
                getString(R.string.thaiEggplantLabel) -> getString(R.string.thaiEggplantTH)
                getString(R.string.turkeyBerryLabel) -> getString(R.string.turkeyBerryTH)
                getString(R.string.kaffirLimeLabel) -> getString(R.string.kaffirLimeTH)
                getString(R.string.gotuKolaLabel) -> getString(R.string.gotuKolaTH)
                getString(R.string.eggplantLabel) -> getString(R.string.eggplantTH)
                getString(R.string.lemonGrassLabel) -> getString(R.string.lemonGrassTH)
                getString(R.string.sweetBasilLabel) -> getString(R.string.sweetBasilTH)
                getString(R.string.holyBasilLabel) -> getString(R.string.holyBasilTH)
                getString(R.string.okraLabel) -> getString(R.string.okraTH)
                getString(R.string.amlaLabel) -> getString(R.string.amlaTH)
                getString(R.string.pepperLabel) -> getString(R.string.pepperTH)
                getString(R.string.pandanLabel) -> getString(R.string.pandanTH)
                getString(R.string.gingerLabel) -> getString(R.string.gingerTH)
                getString(R.string.galangaLabel) -> getString(R.string.galangaTH)
                getString(R.string.starFruitLabel) -> getString(R.string.starFruitTH)
                else -> ""
            }
        }else{
            newTitle = when(title){
                getString(R.string.sesbaniaGrandifloraLabel) -> getString(R.string.sesbaniaGrandifloraENG)
                getString(R.string.thaiEggplantLabel) -> getString(R.string.thaiEggplantENG)
                getString(R.string.turkeyBerryLabel) -> getString(R.string.turkeyBerryENG)
                getString(R.string.kaffirLimeLabel) -> getString(R.string.kaffirLimeENG)
                getString(R.string.gotuKolaLabel) -> getString(R.string.gotuKolaENG)
                getString(R.string.lemonGrassLabel) -> getString(R.string.lemonGrassENG)
                getString(R.string.sweetBasilLabel) -> getString(R.string.sweetBasilENG)
                getString(R.string.holyBasilLabel) -> getString(R.string.holyBasilENG)
                getString(R.string.starFruitLabel) -> getString(R.string.starFruitENG)
                getString(R.string.balsamPearLabel) -> getString(R.string.balsamPearENG)
                else -> title
            }
        }
        return newTitle
    }

    private fun setupEditTextSearch() {
        binding.edittextSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                herbAdaptor.filter.filter(s)
            }
        })
    }

    private fun setupRecycleView() {
        herbAdaptor = HerbListAdaptor(listOfHerbsAll, applicationContext)
        binding.recyclerviewHerb.apply {
            adapter = herbAdaptor
            layoutManager = GridLayoutManager(context, 1)
            setHasFixedSize(true)
        }
    }

    private fun setupAppBar() {
        binding.buttonBar.setOnClickListener {
            if (Prefs.getBoolean(THAI_LANGUAGE_KEY, false)){
                Prefs.putBoolean(THAI_LANGUAGE_KEY, false)
                setupListOfHerbsAllEng()
                herbAdaptor.filter.filter(binding.edittextSearch.text.toString())
                binding.edittextSearch.hint = getString(R.string.search_herbENG)
                binding.buttonBar.text = getString(R.string.TH)
                binding.textviewCameraButton.text = getString(R.string.button_text_cameraENG)
                binding.textviewGalleryButton.text = getString(R.string.button_text_galleryENG)
            }else{
                Prefs.putBoolean(THAI_LANGUAGE_KEY, true)
                setupListOfHerbsAllTH()
                herbAdaptor.filter.filter(binding.edittextSearch.text.toString())
                binding.edittextSearch.hint = getString(R.string.search_herbTH)
                binding.buttonBar.text = getString(R.string.ENG)
                binding.textviewCameraButton.text = getString(R.string.button_text_cameraTH)
                binding.textviewGalleryButton.text = getString(R.string.button_text_galleryTH)
            }
        }
    }
}