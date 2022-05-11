package com.se7sopro.bankmisrchallengetask.di


import com.se7sopro.bankmisrchallengetask.data.repo.currencyData.CurrencyDataRepository
import com.se7sopro.bankmisrchallengetask.data.repo.currencyData.CurrencyDataRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MakOrderRepositoryModule {
    @Binds
    abstract fun providesCurrencyDataRepo(currencyDataRepositoryImp: CurrencyDataRepositoryImp): CurrencyDataRepository

}