package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.room.event.Event
import com.family.happiness.room.event.EventDao
import com.family.happiness.room.photo.PhotoDao

class AlbumRepository(
    private val photoDao: PhotoDao,
    private val eventDao: EventDao,
    private val happinessApi: HappinessApi
) : BaseRepository() {

    val photos = photoDao.getAll()
    val events = eventDao.getAll()

    fun getPhotosByEvent(event: Event) = photoDao.getByEvent(event.id)
}