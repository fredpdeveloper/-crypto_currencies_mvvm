package com.example.cryptos.data.api.model


data class ResponseNews(
    val status: String?,
    val totalResults: Int?,
    val articles: List<Article>?
)