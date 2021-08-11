package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.network.request.MarkAsReadData
import com.family.happiness.network.request.WriteMailData
import com.family.happiness.room.mail.MailDao
import kotlinx.coroutines.flow.distinctUntilChanged

class MailRepository(
    private val mailDao: MailDao,
    private val happinessApi: HappinessApi,
) : BaseRepository() {

    val mailDetails =  mailDao.getAllMailDetail().distinctUntilChanged()

    suspend fun writeMail(
        writeMailData: WriteMailData
    ) = safeApiCall {
        happinessApi.writeMail(writeMailData)
    }

    suspend fun syncMail() = safeApiCall {
        val syncMailResponse = happinessApi.syncMail()
        mailDao.sync(syncMailResponse.mails)
    }

    suspend fun markAsRead(markAsReadData: MarkAsReadData) = safeApiCall {
        happinessApi.markAsRead(markAsReadData)
        mailDao.deleteById(markAsReadData.mailId)
    }

    suspend fun deleteAllData() {
        mailDao.deleteNotIn(emptyList())
    }
}