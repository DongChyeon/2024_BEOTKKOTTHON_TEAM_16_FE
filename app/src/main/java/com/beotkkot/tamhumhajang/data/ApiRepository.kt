package com.beotkkot.tamhumhajang.data

import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiDataSource: ApiDataSource
) {

    fun login(
        nickname: String
    ) = apiDataSource.login(nickname)

    fun getRecommendMarkets(
        userId: Int
    ) = apiDataSource.getRecommendMarkets(userId)

    fun getShops(
        userId: Int
    ) = apiDataSource.getShops(userId)

    fun getQuests(
        userId: Int
    ) = apiDataSource.getQuests(userId)

    fun getBadge(
        userId: Int,
        sequence: Int
    ) = apiDataSource.getBadge(userId, sequence)

    fun getProfile(
        userId: Int
    ) = apiDataSource.getProfile(userId)

    fun getLevelUpPopup(
        userId: Int,
        sequence: Int
    ) = apiDataSource.getLevelUpPopup(userId, sequence)

}