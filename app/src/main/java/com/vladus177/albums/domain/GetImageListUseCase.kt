package com.vladus177.albums.domain

import com.vladus177.albums.common.ResultUseCase
import com.vladus177.albums.data.AlbumsRepository
import com.vladus177.albums.domain.model.ImageModel
import com.vladus177.albums.domain.model.ImagesByAlbumIdParam
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetImageListUseCase @Inject constructor(private val albumsRepository: AlbumsRepository) : ResultUseCase<ImagesByAlbumIdParam, List<ImageModel>>(
    backgroundContext = Dispatchers.IO,
    foregroundContext = Dispatchers.Main
) {
    override suspend fun executeOnBackground(params: ImagesByAlbumIdParam): List<ImageModel>? {
        return albumsRepository.getImagesByAlbumId(params.forceUpdate, params.albumId)
    }
}