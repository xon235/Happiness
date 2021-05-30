package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.network.request.MovePhotoData
import com.family.happiness.room.event.Event
import com.family.happiness.room.event.EventDao
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.photo.PhotoDao
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

    fun getPhotosByEvent(event: Event) = photoDao.getPhotoByEvent(event.id)
    fun getPhotoDetailByUrl(url: String) = photoDao.getPhotoDetailByUrl(url)
//    fun getEventByPhoto(photo: Photo) = eventDao.getEventByPhoto(photo.eventId)

    // Api
    suspend fun uploadPhotos(
        newEvent: Boolean,
        eventName: String,
        tags: List<String>,
        parts: List<MultipartBody.Part>
    ) = safeApiCall {
        val uploadPhotoResponse = happinessApi.uploadPhoto(newEvent, eventName, tags, parts)
        eventDao.insert(listOf(uploadPhotoResponse.event))
        photoDao.insert(uploadPhotoResponse.photos)
    }

    suspend fun movePhoto(photo: Photo, event: Event) = safeApiCall {
        happinessApi.movePhoto(MovePhotoData(photo.url, event.id))
        photoDao.changePhotoEvent(photo.url, event.id)
    }

    suspend fun getPhoto() = safeApiCall {
        val getPhotoResponse = happinessApi.getPhoto()
        eventDao.sync(getPhotoResponse.events)
        photoDao.sync(getPhotoResponse.photos)
    }

    // Dao
//    suspend fun insertEvent(events: List<Event>) = withContext(Dispatchers.IO) {
//        eventDao.insert(events)
//    }
//
//    suspend fun insertPhoto(photos: List<Photo>) = withContext(Dispatchers.IO) {
//        photoDao.insert(photos)
//    }

//    suspend fun changePhotoEvent(photo: Photo, event: Event) = withContext(Dispatchers.IO) {
//        photoDao.changePhotoEvent(photo.url, event.id)
//    }

    suspend fun deletePhoto(photo: Photo) = withContext(Dispatchers.IO) {
        photoDao.delete(photo)
    }

//    suspend fun syncEvent(events: List<Event>) = withContext(Dispatchers.IO) {
//        eventDao.sync(events)
//    }

//    suspend fun syncPhoto(photos: List<Photo>) = withContext(Dispatchers.IO) {
//        photoDao.sync(photos)
//    }
}