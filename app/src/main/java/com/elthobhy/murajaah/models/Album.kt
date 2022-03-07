package com.elthobhy.murajaah.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(

	@field:SerializedName("name_album")
	val nameAlbum: String? = null,

	@field:SerializedName("songs")
	val songs: List<Song>? = null,

	@field:SerializedName("key_album")
	val keyAlbum: String? = null,

	@field:SerializedName("artist_album")
	val artistAlbum: String? = null,

	@field:SerializedName("year_album")
	val yearAlbum: Int? = null,

	@field:SerializedName("image_album")
	val imageAlbum: String? = null
) : Parcelable{
	@Exclude
	fun getAlbum(): String{
		return "Album - $yearAlbum"
	}
}