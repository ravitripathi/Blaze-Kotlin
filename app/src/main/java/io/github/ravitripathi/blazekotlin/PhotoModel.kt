package io.github.ravitripathi.blazekotlin


data class PhotoModel(val urlString: String)
data class PhotoModelDict(val key: String, var model: PhotoModel)
