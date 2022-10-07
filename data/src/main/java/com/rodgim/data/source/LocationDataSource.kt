package com.rodgim.data.source

interface LocationDataSource {
    suspend fun findLastRegion(): String?
}