package com.elthobhy.murajaah.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(

	@field:SerializedName("album_name_song")
	val albumNameSong: String? = null,

	@field:SerializedName("name_song")
	val nameSong: String? = null,

	@field:SerializedName("year_song")
	val yearSong: Int? = null,

	@field:SerializedName("artist_song")
	val artistSong: String? = null,

	@field:SerializedName("uri_song")
	val uriSong: String? = null,

	@field:SerializedName("image_song")
	val imageSong: String? = null,

	@field:SerializedName("key_song")
	val keySong: String? = null
) : Parcelable