package com.dr.fintracker.di

import android.content.Context
import androidx.room.Room
import com.dr.fintracker.data.db.FinTrackerDB
import com.dr.fintracker.other.DefaultWalletProvider
import com.dr.fintracker.other.IDefaultWalletProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideFinTargetDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        FinTrackerDB::class.java,
        "FinTrackerDB"
    ).build()

    @Singleton
    @Provides
    fun provideFinTargetDao(db: FinTrackerDB) = db.getFinTargetDAO()

    @Singleton
    @Provides
    fun provideWalletDao(db: FinTrackerDB) = db.getWalletDAO()

    @Singleton
    @Provides
    fun provideDefaultWallet() : IDefaultWalletProvider{
        return DefaultWalletProvider()
    }
}