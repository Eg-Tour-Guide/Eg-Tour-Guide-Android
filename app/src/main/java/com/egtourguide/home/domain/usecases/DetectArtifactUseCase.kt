package com.egtourguide.home.domain.usecases

import android.content.Context
import android.graphics.Bitmap
import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DetectArtifactUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(
        bitmap: Bitmap,
        context: Context
    ): Flow<ResultWrapper<DetectedArtifact>> {
        val filesDir = context.filesDir
        val file = File(filesDir, "image.jpg")
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        return homeRepository.detectArtifact(body)
    }
}