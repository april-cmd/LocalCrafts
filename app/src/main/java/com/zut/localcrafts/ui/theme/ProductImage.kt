package com.zut.localcrafts.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/**
 * Copies a photo the artisan picked into the app's own storage (shrunk to a
 * sensible size) and returns the file path, which is what we save in Room.
 * Copying means the photo still shows even if the original is deleted from the gallery.
 */
object ImageStorage {

    private const val MAX_SIZE = 1280

    fun saveFromUri(context: Context, uri: Uri): String? {
        return try {
            val resolver = context.contentResolver

            // 1. Read only the size of the picture, so we can shrink it while loading
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

            var sample = 1
            while (bounds.outWidth / sample > MAX_SIZE || bounds.outHeight / sample > MAX_SIZE) {
                sample *= 2
            }

            // 2. Load the shrunken picture
            val options = BitmapFactory.Options().apply { inSampleSize = sample }
            val decoded = resolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
            } ?: return null

            // 3. Fix phone photos that would otherwise show sideways
            val rotation = resolver.openInputStream(uri)?.use { readRotation(it) } ?: 0f
            val bitmap = if (rotation != 0f) {
                Bitmap.createBitmap(
                    decoded, 0, 0, decoded.width, decoded.height,
                    Matrix().apply { postRotate(rotation) }, true
                )
            } else {
                decoded
            }

            // 4. Save as a JPEG inside the app's private folder
            val folder = File(context.filesDir, "product_images").apply { mkdirs() }
            val file = File(folder, "${UUID.randomUUID()}.jpg")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it) }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    private fun readRotation(stream: java.io.InputStream): Float {
        return try {
            val exif = ExifInterface(stream)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        } catch (e: Exception) {
            0f
        }
    }
}

/**
 * Shows the artisan's uploaded photo. If there is no photo (for example the
 * sample products), it shows the emoji on a soft background instead.
 * The caller decides the size and shape with the modifier.
 */
@Composable
fun ProductImage(
    path: String?,
    emoji: String,
    modifier: Modifier = Modifier,
    emojiSize: TextUnit = 38.sp
) {
    // Decode off the main thread so scrolling the list stays smooth
    val bitmap by produceState<ImageBitmap?>(initialValue = null, key1 = path) {
        value = if (path.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) { decodeSampled(path, 600) }
        }
    }

    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        val image = bitmap
        if (image != null) {
            Image(
                bitmap = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(text = emoji, fontSize = emojiSize)
        }
    }
}

private fun decodeSampled(path: String, maxSize: Int): ImageBitmap? {
    val file = File(path)
    if (!file.exists()) return null

    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(path, bounds)

    var sample = 1
    while (bounds.outWidth / sample > maxSize || bounds.outHeight / sample > maxSize) {
        sample *= 2
    }

    val options = BitmapFactory.Options().apply { inSampleSize = sample }
    return BitmapFactory.decodeFile(path, options)?.asImageBitmap()
}