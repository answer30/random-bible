package com.answere2022.random_bible

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        initData()
    }


    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0

            }

        )

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val inNameReveload = remoteConfig.getBoolean("is_name_revealed")
                displayQuotesPager(quotes, inNameReveload)


            }

        }

    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it

            }
        }

        return jsonList.map {
            Quote(
                it.getString("quote"),
                it.getString("name")
            )

        }
    }


    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {


        viewPager.adapter = QuotePagerAdapter(

            quotes

        )


    }
}