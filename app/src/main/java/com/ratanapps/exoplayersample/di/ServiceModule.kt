package com.ratanapps.exoplayersample.di

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.LoadEventInfo
import androidx.media3.exoplayer.source.MediaLoadData
import androidx.media3.exoplayer.upstream.BandwidthMeter
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import androidx.media3.session.MediaSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): SimpleCache {
        val cacheDir = File(context.cacheDir, "media_cache")
        val databaseProvider = StandaloneDatabaseProvider(context)
        return SimpleCache(cacheDir, LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024), databaseProvider)
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideDataSourceFactory(
        @ApplicationContext context: Context,
        cache: SimpleCache
    ): DataSource.Factory {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        val defaultDataSourceFactory = DefaultDataSource.Factory(context, httpDataSourceFactory)
        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        dataSourceFactory: DataSource.Factory
    ): ExoPlayer {
        val bandWidthMeter = DefaultBandwidthMeter.Builder(context).build().apply {
            addEventListener(Handler(Looper.getMainLooper()), object : BandwidthMeter.EventListener {
                override fun onBandwidthSample(
                    elapsedMs: Int,
                    bytesTransferred: Long,
                    bitrateEstimate: Long
                ) {
                    val stringBuilder = StringBuilder().apply {
                        append("Estimated bandwidth: ${bitrateEstimate / 1000} kbps")
                    }


                    Log.d(
                        "HLS_BANDWIDTH",
                        stringBuilder.toString()
                    )
                }
            }
            )
        }

        val mediaSourceFactory = DefaultMediaSourceFactory(context)
            .setDataSourceFactory(dataSourceFactory)
        
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(mediaSourceFactory)
            .setBandwidthMeter(bandWidthMeter)
            .setAudioAttributes(audioAttributes, true)
            .build()
            .apply {

                addAnalyticsListener(object : AnalyticsListener {

                    override fun onBandwidthEstimate(
                        eventTime: AnalyticsListener.EventTime,
                        totalLoadTimeMs: Int,
                        totalBytesLoaded: Long,
                        bitrateEstimate: Long
                    ) {
                        val stringBuilder = StringBuilder()
                        stringBuilder.append("Estimated bandwidth: ${bitrateEstimate / 1000} kbps")
                        stringBuilder.append("Elapsed time: $totalLoadTimeMs ms")


                        Log.d("HLS_DEBUG",
                            stringBuilder.toString()
                        )
                        super.onBandwidthEstimate(
                            eventTime,
                            totalLoadTimeMs,
                            totalBytesLoaded,
                            bitrateEstimate
                        )
                    }



                    override fun onLoadStarted(
                        eventTime: AnalyticsListener.EventTime,
                        loadEventInfo: LoadEventInfo,
                        mediaLoadData: MediaLoadData,
                        retryCount: Int
                    ) {
                        Log.d(
                            "HLS_DEBUG",
                            "LOAD STARTED: " +
                                    "uri=${loadEventInfo.uri} " +
                                    "trackType=${mediaLoadData.trackType} " +
                                    "format=${mediaLoadData.trackFormat}"
                        )
                        super.onLoadStarted(eventTime, loadEventInfo, mediaLoadData, retryCount)
                    }

                    override fun onLoadCompleted(
                        eventTime: AnalyticsListener.EventTime,
                        loadEventInfo: LoadEventInfo,
                        mediaLoadData: MediaLoadData
                    ) {
                        Log.d(
                            "HLS_DEBUG",
                            "LOAD COMPLETED: " +
                                    "uri=${loadEventInfo.uri} " +
                                    "bytes=${loadEventInfo.bytesLoaded} " +
                                    "format=${mediaLoadData.trackFormat}"
                        )
                        super.onLoadCompleted(eventTime, loadEventInfo, mediaLoadData)
                    }

                    override fun onDownstreamFormatChanged(
                        eventTime: AnalyticsListener.EventTime,
                        mediaLoadData: MediaLoadData
                    ) {
                        val format = mediaLoadData.trackFormat
                        Log.d(
                            "HLS_DEBUG",
                            """
                            FORMAT CHANGED
                            ---------------------------
                                    Current Bitrate=${format?.bitrate ?: Format.NO_VALUE}
                                    sampleRate=${format?.sampleRate}
                                    channels=${format?.channelCount}
                            """.trimIndent()

                        )
                        super.onDownstreamFormatChanged(eventTime, mediaLoadData)
                    }
                })
            }
    }


    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession {

        return MediaSession.Builder(context, player).build()
    }
}