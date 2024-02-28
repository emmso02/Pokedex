package fr.imt.pokedex
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import fr.imt.pokedex.PokedexListActivity.Companion.getAssetsDrawable
import fr.imt.pokedex.model.Pokemon
class PokemonCardActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_card_view)
        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                val pokemon: Pokemon? = getParcelable("pokemon")
                if (pokemon != null) {
                    loadPokemon(pokemon)
                } else {
                    finish()
                }
            }
        }
    }
    private fun loadPokemon(myPokemon: Pokemon) {
        with(myPokemon) {
            //Thumbnail
            var bitmap: BitmapDrawable? = getAssetsDrawable(
                context = this@PokemonCardActivity,
                "${String.format("%03d", id)}.png"
            )
            val myPokemonImage: ImageView = findViewById(R.id.pokedex_card_imageView)
            myPokemonImage.setImageDrawable(bitmap)
            //Name
            findViewById<TextView>(R.id.pokemon_card_name).text =
                "#${String.format("%03d", id)} $name"
            //Type1
            bitmap = getAssetsDrawable(context = this@PokemonCardActivity,
                "${type1}.png")
            val myType1Image: ImageView = findViewById(R.id.pokedex_card_type1View)
            myType1Image.setImageDrawable(bitmap)
            //Type2
            val myType2Image: ImageView = findViewById(R.id.pokedex_card_type2View)
            if (type2 != null) {
                bitmap =
                    getAssetsDrawable(context = this@PokemonCardActivity,
                        "${type2}.png")
                myType2Image.setImageDrawable(bitmap)
            }
            findViewById<TextView>(R.id.pokemon_card_description).text = description
        }
    }
}