package com.pankaj.matchmate.di

import android.content.Context
import androidx.room.Room
import com.pankaj.matchmate.network.MatchApi
import com.pankaj.matchmate.repository.db.MatchesDao
import com.pankaj.matchmate.repository.db.MatchesDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.randomuser.me")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun providesMatchApi(retrofit: Retrofit): MatchApi = retrofit.create(MatchApi::class.java)

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MatchesDb =
        Room.databaseBuilder(context, MatchesDb::class.java, "match_db").build()

    @Provides
    fun provideDao(db: MatchesDb): MatchesDao = db.matchDao()
}