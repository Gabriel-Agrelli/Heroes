package com.example.heroes.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.heroes.R
import com.example.heroes.data.model.Character
import com.example.heroes.data.model.ImageVariant
import com.example.heroes.util.getProgressDrawable
import com.example.heroes.util.loadImage
import com.example.heroes.util.prepareImageURL
import kotlinx.android.synthetic.main.item_character.view.*
import okhttp3.internal.notify

class CharactersAdapter(val onCLick: (Character) -> Unit) :
    RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    var characters = ArrayList<Character>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)

        return CharacterViewHolder(view)
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])

        with(holder.itemView) {
            setOnClickListener { onCLick(characters[position]) }
        }
    }

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgCharacter = view.imgCharacter
        private val txtCharacterName = view.txtCharacterName
        private val progressDrawable = getProgressDrawable(view.context)

        fun bind(character: Character) {
            val imgUrl = prepareImageURL(
                character.thumbnail.path,
                ImageVariant.STANDARD_LARGE.string,
                character.thumbnail.extension
            )

            imgCharacter.loadImage(
                imgUrl,
                progressDrawable,
                true
            )
            txtCharacterName.text = character.name
        }
    }

    fun updateData(characters: List<Character>) {
        this.characters.clear()
        this.characters.addAll(characters)
        notifyDataSetChanged()
    }

    fun addData(characters: List<Character>) {
        this.characters.addAll(characters)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.characters.clear()
        notifyDataSetChanged()
    }
}