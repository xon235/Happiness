package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.network.request.WriteMailData
import com.family.happiness.room.mail.MailDao

class MailRepository(
    private val mailDao: MailDao,
    private val happinessApi: HappinessApi,
) : BaseRepository() {

    val mails =  mailDao.getAll()

    suspend fun writeMail(
        writeMailData: WriteMailData
    ) = safeApiCall {
        happinessApi.writeMail(writeMailData)
    }
}