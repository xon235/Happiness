package com.family.happiness.repository

import com.family.happiness.network.HappinessApi
import com.family.happiness.network.request.DeletePhotoData
import com.family.happiness.network.request.MovePhotoData
import com.family.happiness.room.event.Event
import com.family.happiness.room.event.EventDao
import com.family.happiness.room.photo.Photo
import com.family.happiness.room.photo.PhotoDao
import com.family.happiness.room.tag.TagDao
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody

class AlbumRepository(
    private val eventDao: EventDao,
    private val tagDao: TagDao,
    private val photoDao: PhotoDao,
    private val happinessApi: HappinessApi
) : BaseRepository() {

    val photos = photoDao.getAll().distinctUntilChanged()
    val events = eventDao.getAll().distinctUntilChanged()

    fun getPhotosByEvent(event: Event?) = if(event == null) photoDao.getAll() else photoDao.getPhotoByEvent(event.id)
    fun getPhotoDetailByUrl(url: String) = photoDao.getPhotoDetailByUrl(url)

    suspend fun uploadPhotos(
        newEvent: Boolean,
        eventName: String,
        tags: List<String>,
        parts: List<MultipartBody.Part>
    ) = safeApiCall {
        val uploadPhotoResponse = happinessApi.uploadPhoto(newEvent, eventName, tags, parts)
        eventDao.upsert(listOf(uploadPhotoResponse.event))
        tagDao.upsert(uploadPhotoResponse.tags)
        photoDao.insert(uploadPhotoResponse.photos)
    }

    suspend fun movePhoto(photo: Photo, event: Event) = safeApiCall {
        happinessApi.movePhoto(MovePhotoData(listOf(photo.url), event.id))
        photoDao.changePhotoEvent(photo.url, event.id)
        if(photoDao.getPhotoByEvent(photo.eventId).first().isEmpty()){
            eventDao.deleteById(listOf(photo.eventId))
        }
    }

    suspend fun syncPhoto() = safeApiCall {
        val getPhotoResponse = happinessApi.syncPhoto()
        eventDao.sync(getPhotoResponse.events)
        tagDao.sync(getPhotoResponse.tags)
        photoDao.sync(getPhotoResponse.photos)
    }

    suspend fun deletePhoto(photo: Photo) = safeApiCall {
        happinessApi.deletePhoto(DeletePhotoData(listOf(photo.url)))
        photoDao.delete(photo)
        if(photoDao.getPhotoByEvent(photo.eventId).first().isEmpty()){
            eventDao.deleteById(listOf(photo.eventId))
        }
    }

    suspend fun deleteAllData() {
        eventDao.deleteNotIn(emptyList())
        tagDao.deleteNotIn(emptyList())
        photoDao.deleteNotIn(emptyList())
    }
}