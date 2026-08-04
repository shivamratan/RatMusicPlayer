package com.ratanapps.exoplayersample.di

import android.content.Context
import androidx.room.Room
import com.ratanapps.exoplayersample.data.local.MusicDatabase
import com.ratanapps.exoplayersample.data.local.TrackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMusicDatabase(@ApplicationContext context: Context): MusicDatabase {
        return Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            "music_db"
        ).build()
    }

    @Provides
    fun provideTrackDao(db: MusicDatabase): TrackDao {
        return db.trackDao
    }
}