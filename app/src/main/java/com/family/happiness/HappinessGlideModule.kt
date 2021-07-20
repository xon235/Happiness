package com.family.happiness

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import com.bumptech.glide.module.AppGlideModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.InputStream

@GlideModule
class HappinessGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(String::class.java, InputStream::class.java, HeaderLoader.Factory(context))
    }

    class HeaderLoader(val context: Context, concreteLoader: ModelLoader<GlideUrl, InputStream>) :
        BaseGlideUrlLoader<String>(concreteLoader) {
        override fun handles(model: String): Boolean {
            return model.startsWith(context.getString(R.string.server_url))
        }

        override fun getUrl(model: String, width: Int, height: Int, options: Options?): String {
            return model
        }

        override fun getHeaders(
            model: String?,
            width: Int,
            height: Int,
            options: Options?
        ): Headers {
            return Headers {
                val token =
                    runBlocking { context.dataStore.data.map { it[PreferenceKeys.TOKEN] }.first() }
                mapOf("Authorization" to "Bearer $token")
            }
        }

        class Factory(val context: Context) : ModelLoaderFactory<String, InputStream> {

            override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
                return HeaderLoader(
                    context,
                    multiFactory.build(GlideUrl::class.java, InputStream::class.java)
                )
            }

            override fun teardown() {}
        }
    }
}