package com.arny.currencyconvertor.di

import com.arny.currencyconvertor.MainApp
import com.arny.currencyconvertor.data.repositoryimpl.BaseRepositoryImpl
import com.arny.currencyconvertor.data.repositoryimpl.LocalDataSource
import com.arny.currencyconvertor.data.repositoryimpl.RemoteDataSource
import com.arny.domain.usecases.BaseInteractor
import com.arny.domain.usecases.MainInteractor

class DI(application: MainApp) {
    private val mainRepository = BaseRepositoryImpl(application.applicationContext)
    private val remoteRepository = RemoteDataSource()
    private val localRepository = LocalDataSource(mainRepository)
    val baseInteractor = BaseInteractor(mainRepository)
    val mainInteractor = MainInteractor(remoteRepository,mainRepository,localRepository)
}
