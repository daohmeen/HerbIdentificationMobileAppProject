package com.meendaoh.herbidentification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.meendaoh.herbidentification.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        binding.detailTextviewTitle.text = bundle!!.getString("title")
        binding.detailImageview.setImageResource(bundle.getInt("image"))
        binding.detailTextviewDetail.text = bundle.getString("detail")
        binding.detailTextviewBenefit.text = bundle.getString("benefit")
        binding.detailTextviewBenefitDetail.text = bundle.getString("benefitDetail")

    }
}