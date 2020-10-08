package com.example.heroes.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.heroes.R
import com.example.heroes.data.model.Character
import com.example.heroes.data.model.ImageVariant
import com.example.heroes.util.getProgressDrawable
import com.example.heroes.util.loadImage
import com.example.heroes.util.prepareImageURL
import kotlinx.android.synthetic.main.activity_character_details.*

class CharacterDetailsActivity : AppCompatActivity() {
    private var character: Character? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_details)

        val data = intent.extras
        if (data != null) {
            character = data.getParcelable(EXTRA_CHARACTER)

            character?.let {
                supportActionBar?.title = it.name

                val imgUrl = prepareImageURL(
                    it.thumbnail.path,
                    ImageVariant.LANDSCAPE_LARGE.string,
                    it.thumbnail.extension
                )
                val progressDrawable = getProgressDrawable(this)

                imgCharacterBanner.loadImage(imgUrl, progressDrawable)
                txtCharacterName.text = it.name
                txtCharacterDescription.text = it.description
            }
        }
    }

    companion object {
        private const val EXTRA_CHARACTER = "character"
        fun open(context: Context, character: Character) {
            context.startActivity(Intent(context, CharacterDetailsActivity::class.java).apply {
                putExtra(EXTRA_CHARACTER, character)
            })
        }
    }
}