package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.room.event.Event
import com.family.happiness.room.event.EventDao
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.photo.PhotoDao
import com.family.happiness.room.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class AlbumRepository(
    private val photoDao: PhotoDao,
    private val eventDao: EventDao,
    private val happinessApi: HappinessApi
) : BaseRepository() {

    val photos = photoDao.getAll()
    val events = eventDao.getAll()

    fun getPhotoByUrl(url: String) = photoDao.getByUrl(url)

    fun getPhotosByEvent(event: Event) = photoDao.getByEvent(event.id)

    fun getEventByPhoto(photo: Photo) = eventDao.getEventByPhoto(photo.eventId)

    suspend fun changePhotoEvent(photo: Photo, event: Event) = withContext(Dispatchers.IO) {
        photoDao.changePhotoEvent(photo.url, event.id)
    }

    suspend fun deletePhoto(photo: Photo) = withContext(Dispatchers.IO) {
        photoDao.delete(photo)
    }

    suspend fun uploadPhotos(
        newEvent: Boolean,
        eventName: String,
        tags: List<User>,
        parts: List<MultipartBody.Part>
    ) = safeApiCall {
        happinessApi.uploadPhotos(newEvent, eventName, tags, parts)
    }

    suspend fun insertEvents(events: List<Event>){
        eventDao.insert(events)
    }

    suspend fun insertPhotos(photos: List<Photo>){
        photoDao.insert(photos)
    }
}