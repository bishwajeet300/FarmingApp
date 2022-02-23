package com.farmingapp.datasource.preferences

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    abstract fun bindPreferenceManager(
        preferencesManagerImpl: PreferencesManagerImpl
    ): PreferencesManager
}