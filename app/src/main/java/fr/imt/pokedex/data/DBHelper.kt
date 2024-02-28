package fr.imt.pokedex.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import fr.imt.pokedex.model.Pokemon
import fr.imt.pokedex.model.Type

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(POKEMON_CREATE_TABLE_QUERY)
        insertDefaultValues(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(POKEMON_DELETE_TABLE_QUERY)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DB_NAME = "pokemons.db"
        const val DB_VERSION = 3
        const val POKEMON_TABLE = "pokemon"
        const val POKEMON_ID_COLUMN = "ID"
        const val POKEMON_NAME_COLUMN = "NAME"
        const val POKEMON_DESCRIPTION_COLUMN = "DESCRIPTION"
        const val POKEMON_TYPE1_COLUMN = "TYPE1"
        const val POKEMON_TYPE2_COLUMN = "TYPE2"

        const val POKEMON_CREATE_TABLE_QUERY = "create table $POKEMON_TABLE ($POKEMON_ID_COLUMN integer primary key, $POKEMON_NAME_COLUMN text not null, $POKEMON_DESCRIPTION_COLUMN text not null, $POKEMON_TYPE1_COLUMN text not null, $POKEMON_TYPE2_COLUMN text);"
        const val POKEMON_DELETE_TABLE_QUERY = "drop table if exists $POKEMON_TABLE;"

    }

    private fun createPokemonFromCursor(c: Cursor): Pokemon {
        val id = c.getInt(c.getColumnIndexOrThrow(POKEMON_ID_COLUMN))
        val name = c.getString(c.getColumnIndexOrThrow(POKEMON_NAME_COLUMN))
        val description = c.getString(c.getColumnIndexOrThrow(POKEMON_DESCRIPTION_COLUMN))
        val type1Str = c.getString(c.getColumnIndexOrThrow(POKEMON_TYPE1_COLUMN))
        val type2Str = c.getString(c.getColumnIndexOrThrow(POKEMON_TYPE2_COLUMN))
        return if (type2Str != null) {
            Pokemon(id, name, description, Type.valueOf(type1Str), Type.valueOf(type2Str))
        } else {
            Pokemon(id, name, description, Type.valueOf(type1Str), null)
        }
    }

    fun getAllPokemons(): MutableList<Pokemon> {
        val all: MutableList<Pokemon> = mutableListOf<Pokemon>()
        val cursor = readableDatabase.query(
            POKEMON_TABLE,
            null,
            null,
            null,
            null,
            null,
            POKEMON_ID_COLUMN
        )
        with(cursor) {
            while (moveToNext()) {
                val p = createPokemonFromCursor(cursor)
                all.add(p)
            }
        }
        cursor.close()
        return all
    }

    fun getPokemonById(id: Int): Pokemon? {
        val c = readableDatabase.query(
            POKEMON_TABLE,
            null,
            "$POKEMON_ID_COLUMN=?",
            arrayOf<String>("" + id),
            null,
            null,
            POKEMON_ID_COLUMN
        )
        return if (c.moveToFirst()) {
            val p = createPokemonFromCursor(c)
            c.close()
            p
        } else {
            c.close()
            null
        }
    }

    fun addPokemon(db: SQLiteDatabase?, p: Pokemon) {
        val cv = ContentValues()
        cv.put(POKEMON_ID_COLUMN, p.id)
        cv.put(POKEMON_NAME_COLUMN, p.name)
        cv.put(POKEMON_DESCRIPTION_COLUMN, p.description)
        cv.put(POKEMON_TYPE1_COLUMN, p.type1.toString())
        if (p.type2 != null) {
            cv.put(POKEMON_TYPE2_COLUMN, p.type2.toString())
        }
        if (db == null) {
            getWritableDatabase().insert(POKEMON_TABLE, null, cv)
        } else {
            db.insert(POKEMON_TABLE, null, cv)
        }
    }

    private fun insertDefaultValues(db: SQLiteDatabase) {
        // addPokemon(new Pokemon(1, "Bulbasaur", Type.POISON, Type.GRASS,
        // "Description..."), db);
        addPokemon(
            db,
            Pokemon(
                1,
                "Bulbasaur",
                "A strange seed was planted on its back at birth. The plant sprouts and grows with this Pokémon.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                2,
                "Ivysaur",
                "When the bulb on its back grows large, it appears to lose the ability to stand on its hind legs.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                3,
                "Venusaur",
                "The plant blooms when it is asorbing solar energy. It stays on the move to seek sunlight.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                4,
                "Charmander",
                "Obviously prefers hot places. When it rains, steam is said to spout from the tip of its tail.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                5,
                "Charmeleon",
                "When it swings its burning tail, it elevates the temperature to unbearably high levels.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                6,
                "Charizard",
                "It spits fire that is hot enough to melt boulders. Known to cause forest fires unintentionally.",
                Type.FIRE, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                7,
                "Squirtle",
                "After birth, its back swells and hardens into a shell. Powerfully sprays foam from its mouth.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                8,
                "Wartortle",
                "Often hides in water to stalk unwary prey. For swimming fast, it moves its ears to maintain balance.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                9,
                "Blastoise",
                "A brutal Pokémon with pressurized water jets on its shell. They are used for high speed tackles.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                10,
                "Caterpie",
                "Its short feet are tipped with suction pads that enable it to tirelessly climb slopes and walls.",
                Type.BUG, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                11,
                "Metapod",
                "This Pokémon is vulnerable to attack while its shell is soft, exposing its weak and tender body.",
                Type.BUG, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                12,
                "Butterfree",
                "In battle, it flaps its wings at high speed, releasing highly toxic dust into the air.",
                Type.BUG, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                13,
                "Weedle",
                "Often found in forests, eating leaves. It has a sharp, venomous stinger on its head.",
                Type.BUG, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                14,
                "Kakuna",
                "Almost incapable of moving, this Pokémon can only harden its shell to protect itself from predators.",
                Type.BUG, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                15,
                "Beedrill",
                "Flies at high speed and attacks using its large venomous stingers on its forelegs and tail.",
                Type.BUG, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                16,
                "Pidgey",
                "A common sight in forests and woods. It flaps its wings at ground level to kick up blinding sand.",
                Type.NORMAL, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                17,
                "Pidgeotto",
                "Very protective of its sprawling territory, this Pokémon will fiercely peck at any intruder.",
                Type.NORMAL, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                18,
                "Pidgeot",
                "When hunting, it skims the surface of water at high speed to pick off unwary prey such as Magikarp.",
                Type.NORMAL, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                19,
                "Rattata",
                "Bites anything when it attacks. Small and very quick, it is a common sight in many places.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                20,
                "Raticate",
                "It uses its whiskers to maintain its balance and will slow down if they are cut off.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                21,
                "Spearow",
                "Eats bugs in grassy areas. It has to flap its short wings at high speed to stay airborne.",
                Type.NORMAL, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                22,
                "Fearow",
                "With its huge and magnificent wings, it can keep aloft without ever having to land for rest.",
                Type.NORMAL, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                23,
                "Ekans",
                "Moves silently and stealthily. Eats the eggs of birds, such as Pidgey and Spearow, whole.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                24,
                "Arbok",
                "It is rumored that the ferocious warning markings on its belly differ from area to area.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                25,
                "Pikachu",
                "When several of these Pokémon gather, their electricity could build and cause lightning storms.",
                Type.ELECTRIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                26,
                "Raichu",
                "Its long tail serves as a ground to protect itself from its own high voltage power.",
                Type.ELECTRIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                27,
                "Sandshrew",
                "Burrows deep underground in arid locations far from water. It only emerges to hunt for food.",
                Type.GROUND, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                28,
                "Sandslash",
                "Curls up into a spiny ball when threatened. It can roll while curled up to attack or escape.",
                Type.GROUND, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                29,
                "Nidoran F",
                "Although small, its venomous barbs render this Pokémon dangerous. The female has smaller horns.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                30,
                "Nidorina",
                "The female's horn develops slowly. Prefers physical attacks such as clawing and biting.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                31,
                "Nidoqueen",
                "Its hard scales provide strong protection. It uses its hefty bulk to execute powerful moves.",
                Type.POISON, Type.GROUND
            )
        )
        addPokemon(
            db,
            Pokemon(
                32,
                "Nidoran M",
                "Stiffens its ears to sense danger. The larger its horns, the more powerful its secreted venom.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                33,
                "Nidorino",
                "An aggressive Pokémon that is quick to attack. The horn on its head secretes a powerful venom.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                34,
                "Nidoking",
                "It uses its powerful tail in battle to smash, constrict, then break the prey's bones.",
                Type.POISON, Type.GROUND
            )
        )
        addPokemon(
            db,
            Pokemon(
                35,
                "Clefairy",
                "Its magical and cute appeal has many admirers. It is rare and found only in certain areas.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                36,
                "Clefable",
                "A timid fairy Pokémon that is rarely seen. It will run and hide the moment it senses people.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                37,
                "Vulpix",
                "At the time of birth, it has just one tail. The tail splits from its tip as it grows older.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                38,
                "Ninetales",
                "Very smart and very vengeful. Grabbing one of its many tails could result in a 1000-year curse.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                39,
                "Jigglypuff",
                "When its huge eyes light up, it sings a mysteriously soothing melody that lulls its enemies to sleep.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                40,
                "Wigglytuff",
                "The body is soft and rubbery. When angered, it will suck in air and inflate itself to an enormous size.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                41,
                "Zubat",
                "Forms colonies in perpetually dark places. Uses ultrasonic waves to identify and approach targets.",
                Type.POISON, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                42,
                "Golbat",
                "Once it strikes, it will not stop draining energy from the victim even if it gets too heavy to fly.",
                Type.POISON, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                43,
                "Oddish",
                "During the day, it keeps its face buried in the ground. At night, it wanders around sowing its seeds.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                44,
                "Gloom",
                "The fluid that oozes from its mouth isn't drool. It is a nectar that is used to attract prey.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                45,
                "Vileplume",
                "The larger its petals, the more toxic pollen it contains. Its big head is heavy and hard to hold up.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                46,
                "Paras",
                "Burrows to suck tree roots. The mushrooms on its back grow by drawing nutrients from the bug host.",
                Type.BUG, Type.GRASS
            )
        )
        addPokemon(
            db,
            Pokemon(
                47,
                "Parasect",
                "A host-parasite pair in which the parasite mushroom has taken over the host bug. Prefers damp places.",
                Type.BUG, Type.GRASS
            )
        )
        addPokemon(
            db,
            Pokemon(
                48,
                "Venonat",
                "Lives in the shadows of tall trees where it eats bugs. It is attracted by light at night.",
                Type.BUG, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                49,
                "Venomoth",
                "The dustlike scales covering its wings are color-coded to indicate the kinds of poison it has.",
                Type.BUG, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                50,
                "Diglett",
                "Lives about one yard underground where it feeds on plant roots. It sometimes appears aboveground.",
                Type.GROUND, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                51,
                "Dugtrio",
                "A team of Diglett triplets. It triggers huge earthquakes by burrowing 60 miles underground.",
                Type.GROUND, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                52,
                "Meowth",
                "Adores circular objects. Wanders the street on a nightly basis to look for dropped loose change.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                53,
                "Persian",
                "Although its fur has many admirers, it is tough to raise as a pet because of its fickle meanness.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                54,
                "Psyduck",
                "While lulling its enemies with its vacant look, this wily Pokémon will use psychokinetic powers.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                55,
                "Golduck",
                "Often seen swimming elegantly by lakeshores. It is often mistaken for the Japanese monster Kappa.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                56,
                "Mankey",
                "Extremely quick to anger. It could be docile one moment then thrashing away the next instant.",
                Type.FIGHTING, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                57,
                "Primeape",
                "Always furious and tenacious to boot. It will not abandon chasing its quarry until it is caught.",
                Type.FIGHTING, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                58,
                "Growlithe",
                "Very protective of its territory. It will bark and bite to repel intruders from its space.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                59,
                "Arcanine",
                "A Pokémon that has been admired since the past for its beauty. It runs agilely as if on wings.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                60,
                "Poliwag",
                "Its newly grown legs prevent it from walking well. It appears to prefer swimming over walking.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                61,
                "Poliwhirl",
                "It can live in or out of water. When out of water, it constantly sweats to keep its body slimy.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                62,
                "Poliwrath",
                "A swimmer adept at both the front crawl and breaststroke. Easily overtakes the best human swimmers.",
                Type.FIGHTING, Type.WATER
            )
        )
        addPokemon(
            db,
            Pokemon(
                63,
                "Abra",
                "Using its ability to read minds, it will sense impending danger and teleport to safety.",
                Type.PSYCHIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                64,
                "Kadabra",
                "It emits special alpha waves from its body that induce headaches just by being close by.",
                Type.PSYCHIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                65,
                "Alakazam",
                "Its brain can outperform a super-computer. Its intelligence quotient is said to be 5,000.",
                Type.PSYCHIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                66,
                "Machop",
                "Loves to build its muscles. It trains in all styles of martial arts to become even stronger.",
                Type.FIGHTING, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                67,
                "Machoke",
                "Its muscular body is so powerful, it must wear a power-save belt to be able to regulate its motions.",
                Type.FIGHTING, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                68,
                "Machamp",
                "Using its heavy muscles, it throws powerful punches that can send the victim clear over the horizon.",
                Type.FIGHTING, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                69,
                "Bellsprout",
                "A carnivorous Pokémon that traps and eats bugs. It appears to use its root feet to replenish moisture.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                70,
                "Weepinbell",
                "It spits out poisonpowder to immobilize the enemy and then finishes it with a spray of acid.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                71,
                "Victreebel",
                "Said to live in huge colonies deep in jungles, although no one has ever returned from there.",
                Type.GRASS, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                72,
                "Tentacool",
                "Drifts in shallow seas. Anglers who hook them by accident are often punished by its stinging acid.",
                Type.WATER, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                73,
                "Tentacruel",
                "The tentacles are normally kept short. On hunts, they are extended to ensnare and immobilize prey.",
                Type.WATER, Type.POISON
            )
        )
        addPokemon(
            db,
            Pokemon(
                74,
                "Geodude",
                "Found in fields and mountains. Mistaking them for boulders, people often step or trip on them.",
                Type.ROCK, Type.GROUND
            )
        )
        addPokemon(
            db,
            Pokemon(
                75,
                "Graveler",
                "Rolls down slopes to move. It rolls over any obstacle without slowing or changing its direction.",
                Type.ROCK, Type.GROUND
            )
        )
        addPokemon(
            db,
            Pokemon(
                76,
                "Golem",
                "Its boulder-like body is extremely hard. It can easily withstand dynamite blasts without taking damage.",
                Type.ROCK, Type.GROUND
            )
        )
        addPokemon(
            db,
            Pokemon(
                77,
                "Ponyta",
                "Its hooves are 10 times harder than diamonds. It can trample anything completely flat in moments.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                78,
                "Rapidash",
                "Very competitive, this Pokémon will chase anything that moves fast in the hopes of racing it.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                79,
                "Slowpoke",
                "Incredibly slow and dopey. It takes 5 seconds for it to feel pain when under attack.",
                Type.WATER, Type.PSYCHIC
            )
        )
        addPokemon(
            db,
            Pokemon(
                80,
                "Slowbro",
                "The Shellder that latches onto Slowpoke's tail is said to feed on the host's leftover scraps.",
                Type.WATER, Type.PSYCHIC
            )
        )
        addPokemon(
            db,
            Pokemon(
                81,
                "Magnemite",
                "Uses antigravity to stay suspended. Appears without warning and uses Thunder Wave and similar moves.",
                Type.ELECTRIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                82,
                "Magneton",
                "Formed by several Magnemite linked together. They frequently appear when sunspots flare up.",
                Type.ELECTRIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                83,
                "Farfetch'd",
                "The sprig of green onions it holds is its weapon. It is used much like a metal sword.",
                Type.NORMAL, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                84,
                "Doduo",
                "A bird that makes up for its poor flying with its fast foot speed. Leaves giant footprints.",
                Type.NORMAL, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                85,
                "Dodrio",
                "Uses its three brains to execute complex plans. While two heads sleep, one head is said to stay awake.",
                Type.NORMAL, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                86,
                "Seel",
                "The protruding horn on its head is very hard. It is used for bashing through thick ice.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                87,
                "Dewgong",
                "Stores thermal energy in its body. Swims at a steady 8 knots even in intensely cold waters.",
                Type.WATER, Type.ICE
            )
        )
        addPokemon(
            db,
            Pokemon(
                88,
                "Grimer",
                "Appears in filthy areas. Thrives by sucking up polluted sludge that is pumped out of factories.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                89,
                "Muk",
                "Thickly covered with a filthy, vile sludge. It is so toxic, even its footprints contain poison.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                90,
                "Shellder",
                "Its hard shell repels any kind of attack. It is vulnerable only when its shell is open.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                91,
                "Cloyster",
                "When attacked, it launches its horns in quick volleys. Its innards have never been seen.",
                Type.WATER, Type.ICE
            )
        )
        addPokemon(
            db,
            Pokemon(
                92,
                "Gastly",
                "Almost invisible, this gaseous Pokémon cloaks the target and puts it to sleep without notice.",
                Type.POISON, Type.GHOST
            )
        )
        addPokemon(
            db,
            Pokemon(
                93,
                "Haunter",
                "Because of its ability to slip through block walls, it is said to be from another dimension.",
                Type.POISON, Type.GHOST
            )
        )
        addPokemon(
            db,
            Pokemon(
                94,
                "Gengar",
                "Under a full moon, this Pokémon likes to mimic the shadows of people and laugh at their fright.",
                Type.POISON, Type.GHOST
            )
        )
        addPokemon(
            db,
            Pokemon(
                95,
                "Onix",
                "As it grows, the stone portions of its body harden to become similar to a diamond, but colored black.",
                Type.ROCK, Type.GROUND
            )
        )
        addPokemon(
            db,
            Pokemon(
                96,
                "Drowzee",
                "Puts enemies to sleep, then eats their dreams. Occasionally gets sick from eating bad dreams.",
                Type.PSYCHIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                97,
                "Hypno",
                "When it locks eyes with an enemy, it will use a mix of PSI moves such as Hypnosis and Confusion.",
                Type.PSYCHIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                98,
                "Krabby",
                "Its pincers are not only powerful weapons, they are used for balance when walking sideways.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                99,
                "Kingler",
                "The large pincer has 10,000-horsepower crushing force. However, its huge size makes it unwieldy to use.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                100,
                "Voltorb",
                "Usually found in power plants. Easily mistaken for a Poké Ball, it has zapped many people.",
                Type.ELECTRIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                101,
                "Electrode",
                "It stores electric energy under very high pressure. It often explodes with little or no provocation.",
                Type.ELECTRIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                102,
                "Exeggcute",
                "It is often mistaken for eggs. When disturbed, they gather quickly and attack in swarms.",
                Type.GRASS, Type.PSYCHIC
            )
        )
        addPokemon(
            db,
            Pokemon(
                103,
                "Exeggutor",
                "Legend has it that on rare occasions, one of its heads will drop off and continue on as an Exeggcute.",
                Type.GRASS, Type.PSYCHIC
            )
        )
        addPokemon(
            db,
            Pokemon(
                104,
                "Cubone",
                "Because it never removes its skull helmet, no one has ever seen this Pokémon's real face.",
                Type.GROUND, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                105,
                "Marowak",
                "The bone it holds is its key weapon. It throws the bone skillfully like a boomerang to KO targets.",
                Type.GROUND, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                106,
                "Hitmonlee",
                "When in a hurry, its legs lengthen progressively. It runs smoothly with extra long, loping strides.",
                Type.FIGHTING, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                107,
                "Hitmonchan",
                "While apparently doing nothing, it fires punches in lightning fast volleys that are impossible to see.",
                Type.FIGHTING, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                108,
                "Lickitung",
                "Its tongue can be extended like a chameleon's. It leaves a tingling sensation when it licks enemies.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                109,
                "Koffing",
                "Because it stores several kinds of toxic gases in its body, it is prone to exploding without warning.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                110,
                "Weezing",
                "Where two kinds of poison gases meet, two Koffings can fuse into a Weezing over many years.",
                Type.POISON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                111,
                "Rhyhorn",
                "Its massive bones are 1000 times harder than human bones. It can easily knock a trailer flying.",
                Type.ROCK, Type.GROUND
            )
        )
        addPokemon(
            db,
            Pokemon(
                112,
                "Rhydon",
                "Protected by an armor-like hide, it is capable of living in molten lava of 3,600 degrees.",
                Type.ROCK, Type.GROUND
            )
        )
        addPokemon(
            db,
            Pokemon(
                113,
                "Chansey",
                "A rare and elusive Pokémon that is said to bring happiness to those who manage to get it.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                114,
                "Tangela",
                "The whole body is swathed with wide vines that are similar to seaweed. The vines sway as it walks.",
                Type.GRASS, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                115,
                "Kangaskhan",
                "The infant rarely ventures out of its mother's protective pouch until it is 3 years old.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                116,
                "Horsea",
                "Known to shoot down flying bugs with precision blasts of ink from the surface of the water.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                117,
                "Seadra",
                "Capable of swimming backwards by rapidly flapping its wing-like pectoral fins and stout tail.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                118,
                "Goldeen",
                "Its tail fin billows like an elegant ballroom dress, giving it the nickname of the Water Queen.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                119,
                "Seaking",
                "In the autumn spawning season, they can be seen swimming powerfully up rivers and creeks.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                120,
                "Staryu",
                "An enigmatic Pokémon that can effortlessly regenerate any appendage it loses in battle.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                121,
                "Starmie",
                "Its central core glows with the seven colors of the rainbow. Some people value the core as a gem.",
                Type.WATER, Type.PSYCHIC
            )
        )
        addPokemon(
            db,
            Pokemon(
                122,
                "M. Mime",
                "If interrupted while it is miming, it will slap around the offender with its broad hands.",
                Type.PSYCHIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                123,
                "Scyther",
                "With ninja-like agility and speed, it can create the illusion that there is more than one.",
                Type.BUG, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                124,
                "Jynx",
                "It seductively wiggles its hips as it walks. It can cause people to dance in unison with it.",
                Type.ICE, Type.PSYCHIC
            )
        )
        addPokemon(
            db,
            Pokemon(
                125,
                "Electabuzz",
                "Normally found near power plants, they can wander away and cause major blackouts in cities.",
                Type.ELECTRIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                126,
                "Magmar",
                "Its body always burns with an orange glow that enables it to hide perfectly among flames.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                127,
                "Pinsir",
                "If it fails to crush the victim in its pincers, it will swing it around and toss it hard.",
                Type.BUG, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                128,
                "Tauros",
                "When it targets an enemy, it charges furiously while whipping its body with its long tails.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                129,
                "Magikarp",
                "In the distant past, it was somewhat stronger than the horribly weak descendants that exist today.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                130,
                "Gyarados",
                "Rarely seen in the wild. Huge and vicious, it is capable of destroying entire cities in a rage.",
                Type.WATER, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                131,
                "Lapras",
                "A Pokémon that has been overhunted almost to extinction. It can ferry people across the water.",
                Type.WATER, Type.ICE
            )
        )
        addPokemon(
            db,
            Pokemon(
                132,
                "Ditto",
                "Capable of copying an enemy's genetic code to instantly transform itself into a duplicate of the enemy.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                133,
                "Eevee",
                "Its genetic code is irregular. It may mutate if it is exposed to radiation from element stones.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                134,
                "Vaporeon",
                "Lives close to water. Its long tail is ridged with a fin which is often mistaken for a mermaid's.",
                Type.WATER, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                135,
                "Jolteon",
                "It accumulates negative ions in the atmosphere to blast out 10000-volt lightning bolts.",
                Type.ELECTRIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                136,
                "Flareon",
                "When storing thermal energy in its body, its temperature could soar to over 1,600 degrees.",
                Type.FIRE, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                137,
                "Porygon",
                "A Pokémon that consists entirely of programming code. Capable of moving freely in cyberspace.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                138,
                "Omanyte",
                "Although long extinct, in rare cases, it can be genetically resurrected from fossils.",
                Type.ROCK, Type.WATER
            )
        )
        addPokemon(
            db,
            Pokemon(
                139,
                "Omastar",
                "A prehistoric Pokémon that died out when its heavy shell made it impossible to catch prey.",
                Type.ROCK, Type.WATER
            )
        )
        addPokemon(
            db,
            Pokemon(
                140,
                "Kabuto",
                "A Pokémon that was resurrected from a fossil found in what was once the ocean floor eons ago.",
                Type.ROCK, Type.WATER
            )
        )
        addPokemon(
            db,
            Pokemon(
                141,
                "Kabutops",
                "Its sleek shape is perfect for swimming. It slashes prey with its claws and drains the body fluids.",
                Type.ROCK, Type.WATER
            )
        )
        addPokemon(
            db,
            Pokemon(
                142,
                "Aerodactyl",
                "A ferocious, prehistoric Pokémon that goes for the enemy's throat with its serrated saw-like fangs.",
                Type.ROCK, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                143,
                "Snorlax",
                "Very lazy. Just eats and sleeps. As its rotund bulk builds, it becomes steadily more slothful.",
                Type.NORMAL, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                144,
                "Articuno",
                "A legendary bird Pokémon said to appear to doomed people who are lost in icy mountains.",
                Type.ICE, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                145,
                "Zapdos",
                "A legendary bird Pokémon that is said to appear from clouds while dropping enormous lightning bolts.",
                Type.ELECTRIC, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                146,
                "Moltres",
                "Known as the legendary bird of fire. Every flap of its wings creates a giant dazzle of flashing flames.",
                Type.FIRE, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                147,
                "Dratini",
                "Long considered a mythical Pokémon until recently, when a small colony was found living underwater.",
                Type.DRAGON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                148,
                "Dragonair",
                "A mystical Pokémon that exudes a gentle aura. Has the ability to change climate conditions.",
                Type.DRAGON, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                149,
                "Dragonite",
                "An extremely rarely seen marine Pokémon. Its intelligence is said to match that of humans.",
                Type.DRAGON, Type.FLYING
            )
        )
        addPokemon(
            db,
            Pokemon(
                150,
                "Mewtwo",
                "It was created by a scientist after years of horrific gene splicing and DNA engineering experiments.",
                Type.PSYCHIC, null
            )
        )
        addPokemon(
            db,
            Pokemon(
                151,
                "Mew",
                "So rare that it is still said to be a mirage by many experts. Only a few people have seen it worldwide.",
                Type.PSYCHIC, null
            )
        )
    }


}