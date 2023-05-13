package com.example.music.data.repository

import com.example.music.data.Resource
import com.example.music.data.dto.frames.DataFrames
import com.example.music.data.dto.localprank.MyFolderAudio
import com.example.music.data.dto.localprank.MyFolderImage
import com.example.music.data.dto.localprank.MyFolderVideo
import com.example.music.data.dto.localprank.MyVideo
import com.example.music.data.dto.login.LoginRequest
import com.example.music.data.dto.login.LoginResponse
import com.example.music.data.dto.recipes.Recipes
import com.example.music.data.dto.response.*
import com.example.music.data.local.LocalData
import com.example.music.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


/**
 * Created by TruyenIT
 */

class DataRepository @Inject constructor(private val remoteRepository: RemoteData, private val localRepository: LocalData, private val ioDispatcher: CoroutineContext) :
    DataRepositorySource {

    override suspend fun requestRecipes(): Flow<Resource<Recipes>> {
        return flow {
            emit(remoteRepository.requestRecipes())
        }.flowOn(ioDispatcher)
    }

    override suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return flow {
            emit(localRepository.doLogin(loginRequest))
        }.flowOn(ioDispatcher)
    }

    override suspend fun addToFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            localRepository.getCachedFavourites().let {
                it.data?.toMutableSet()?.let { set ->
                    val isAdded = set.add(id)
                    if (isAdded) {
                        emit(localRepository.cacheFavourites(set))
                    } else {
                        emit(Resource.Success(false))
                    }
                }
                it.errorCode?.let { errorCode ->
                    emit(Resource.DataError<Boolean>(errorCode))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.removeFromFavourites(id))
            emit(localRepository.removeFromFavourites(id))
        }.flowOn(ioDispatcher)
    }

    override suspend fun isFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localRepository.isFavourite(id))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestFrames(): Flow<Resource<DataFrames>> {
        return flow {
            emit(remoteRepository.requestFrames())
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestFramesImage(): Flow<Resource<DataFrames>> {
        return flow {
            emit(remoteRepository.requestDataFramesImage())
        }.flowOn(ioDispatcher)
    }


    override suspend fun requestCategorySound(filter: String): Flow<Resource<ResponseCategorySound>> {
        return flow {
            emit(remoteRepository.requestSoundCategory(filter))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestSound(filter: String): Flow<Resource<ResponseSound>> {
        return flow {
            emit(remoteRepository.requestSound(filter))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestVideo(filter: String): Flow<Resource<ResponseVideo>> {
        return flow {
            emit(remoteRepository.requestVideo(filter))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestCall(filter: String): Flow<Resource<ResponsePrankCall>> {
        return flow {
            emit(remoteRepository.requestCall(filter))
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAllImage(): Flow<Resource<List<MyFolderImage>>> {
        return flow {
            emit(localRepository.getAllImage())
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAllAudio(): Flow<Resource<List<MyFolderAudio>>> {
        return flow {
            emit(localRepository.getAllAudio())
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAllVideo(): Flow<Resource<List<MyFolderVideo>>> {
        return flow {
            emit(localRepository.getAllVideo())
        }.flowOn(ioDispatcher)
    }

    override suspend fun getAllVideoFromFolder(path: String): Flow<Resource<List<MyVideo>>> {
        return flow {
            emit(localRepository.getAllVideoFromFolder(path))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestCategoryGif(filter: String): Flow<Resource<ResponsePrankRecordFolder>> {
        return flow {
            emit(remoteRepository.requestCategoryGif(filter))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestCategoryVideo(filter: String): Flow<Resource<ResponsePrankRecordFolder>> {
        return flow {
            emit(remoteRepository.requestCategoryVideo(filter))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestItemGif(filter: String): Flow<Resource<ResponsePrankRecordItem>> {
        return flow {
            emit(remoteRepository.requestItemGif(filter))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestItemVideo(filter: String): Flow<Resource<ResponsePrankRecordItem>> {
        return flow {
            emit(remoteRepository.requestItemVideo(filter))
        }.flowOn(ioDispatcher)
    }

    override suspend fun signup(
        email: String,
        password: String
    ){

    }

    override suspend fun requestNewReleaseSong(page: Int, limit: Int, order: String):
            Flow<Resource<ResponseSong>> {
        return flow{
            emit(remoteRepository.requestDataNewReleaseSong(page, limit, order))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestTopTrendingSong(
        page: Int,
        limit: Int,
        order: String
    ): Flow<Resource<ResponseSong>> {
        return flow{
            emit(remoteRepository.requestDataTopTrendingSong(page, limit, order))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestTopDownLoadSong(
        page: Int,
        limit: Int,
        order: String
    ): Flow<Resource<ResponseSong>> {
        return flow{
            emit(remoteRepository.requestDataTopDownLoadSong(page, limit, order))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestGenres(): Flow<Resource<ResponseGenres>> {
        return flow{
            emit(remoteRepository.requestDataGenres())
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestPhotoAbove(): Flow<Resource<ResponseGenres>> {
        return flow{
            emit(remoteRepository.requestDataPhotoAbove())
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestSearchSong(
        page: Int,
        limit: Int,
        name: String
    ): Flow<Resource<ResponseSong>> {
        return flow{
            emit(remoteRepository.requestDataSearchSong(page, limit,name))
        }.flowOn(ioDispatcher)
    }
}
