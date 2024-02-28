package fr.imt.pokedex.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Pokemon(val id: Int, val name: String, val description: String, val type1: Type, val type2: Type?): Parcelable
