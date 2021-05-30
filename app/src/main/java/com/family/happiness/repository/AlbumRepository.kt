package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.network.request.DeletePhotoData
import com.family.happiness.network.request.MovePhotoData
import com.family.happiness.room.event.Event
import com.family.happiness.room.event.EventDao
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.photo.PhotoDao
import com.family.happiness.room.tag.TagDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class AlbumRepository(
    private val eventDao: EventDao,
    private val tagDao: TagDao,
    private val photoDao: PhotoDao,
    private val happinessApi: HappinessApi
) : BaseRepository() {

    val photos = photoDao.getAll()
    val events = eventDao.getAll()

    fun getPhotosByEvent(event: Event) = photoDao.getPhotoByEvent(event.id)
    fun getPhotoDetailByUrl(url: String) = photoDao.getPhotoDetailByUrl(url)

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

    suspend fun syncPhoto() = safeApiCall {
        val getPhotoResponse = happinessApi.syncPhoto()
        eventDao.sync(getPhotoResponse.events)
        tagDao.sync(getPhotoResponse.tags)
        photoDao.sync(getPhotoResponse.photos)
    }

    suspend fun deletePhoto(photo: Photo) = safeApiCall {
        happinessApi.deletePhoto(DeletePhotoData(photo.url))
        photoDao.delete(photo)
    }
}