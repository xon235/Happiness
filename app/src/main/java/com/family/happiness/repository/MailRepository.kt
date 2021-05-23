package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.network.response.WriteMailData

class MailRepository(
    private val happinessApi: HappinessApi
) : BaseRepository() {

    suspend fun writeMail(
        writeMailData: WriteMailData
    ) = safeApiCall {
        happinessApi.writeMail(writeMailData)
    }
}