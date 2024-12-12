package com.dicoding.dermata.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("websites")
	val websites: List<WebsitesItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class WebsitesItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
