package fr.imt.pokedex
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fr.imt.pokedex.adapter.PokemonListAdapter
import fr.imt.pokedex.data.Datasource
import fr.imt.pokedex.model.Pokemon
import java.io.IOException

const val TAG: String = "PokedexListActivity"
class PokedexListActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex_list)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
        val dataset: List<Pokemon> = Datasource().loadPokemons()
        recyclerView.adapter = PokemonListAdapter(this, dataset)
    }
    companion object {
        fun getAssetsDrawable(context: Context, fileName: String): BitmapDrawable? {
            var bitmap: BitmapDrawable? = null
            try
            {
                val inputStream = context.assets.open(fileName)
                bitmap = BitmapDrawable(context.resources,
                    BitmapFactory.decodeStream(inputStream))
            } catch (e: IOException)
            {
                Log.e(TAG, "Error occurred retrieving file $fileName - ${e.message}")
                e.printStackTrace()
            }
            return bitmap
        }
    }
}