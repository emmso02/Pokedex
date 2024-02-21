package fr.imt.pokedex.data

import fr.imt.pokedex.model.Pokemon
import fr.imt.pokedex.model.Type
class Datasource {
    fun loadPokemons(): List<Pokemon> {
        return listOf(
            Pokemon(1,"Bulbasaur",
                "A strange seed was planted on its back at birth. The plant sprouts and grows with this Pokémon.",
                        Type.GRASS, Type.POISON),
            Pokemon(
                2,
                "Ivysaur",
                "When the bulb on its back grows large, it appears to lose the ability to stand on its hind legs.",
                        Type.GRASS, Type.POISON
            ),
            Pokemon(
                3,
                "Venusaur",
                "The plant blooms when it is asorbing solar energy. It stays on the move to seek sunlight.",
                        Type.GRASS, Type.POISON
            ),
            Pokemon(
                4,
                "Charmander",
                "Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.",
                        Type.FIRE, null
            ),
            Pokemon(
                5,
                "Charmeleon",
                "When it swings its burning tail, it elevates the temperature to unbearably high levels.",
                        Type.FIRE, null
            ),
            Pokemon(
                6,
                "Charizard",
                "It spits fire that is hot enough to melt boulders. Known to cause forest fires unintentionally.",
                        Type.FIRE, Type.FLYING
            ),
            Pokemon(7,
                "Squirtle",
                "After birth, its back swells and hardens into a shell. Powerfully sprays foam from its mouth.",
                        Type.WATER, null
            ),
            Pokemon(
                8,
                "Wartortle",
                "Often hides in water to stalk unwary prey. For swimming fast, it moves its ears to maintain balance.",
                        Type.WATER, null
            ),
            Pokemon(
                9,
                "Blastoise",
                "A brutal Pokémon with pressurized water jets on its shell. They are used for high speed tackles.",
                        Type.WATER, null
            )
        )
    }
}
