package com.termix.core.proot

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Installs a Linux RootFS for use with PRoot.
 *
 * Current implementation is a **stub** that simulates download + extract.
 * Replace [downloadAndExtract] with real OCI / tarball logic later.
 */
class ProotInstaller(private val context: Context) {

    sealed class Progress {
        data class Downloading(val percent: Int, val message: String) : Progress()
        data class Extracting(val percent: Int, val message: String) : Progress()
        data class Done(val rootfsPath: String) : Progress()
        data class Error(val message: String) : Progress()
    }

    fun install(distro: Distro): Flow<Progress> = flow {
        try {
            val rootfsDir = File(context.filesDir, "rootfs/${distro.id}")
            if (!rootfsDir.exists()) {
                rootfsDir.mkdirs()
            }

            // --- STUB: simulate download ---
            for (i in 0..100 step 10) {
                emit(Progress.Downloading(i, "Downloading ${distro.displayName}… $i%"))
                delay(150)
            }

            // --- STUB: simulate extract ---
            for (i in 0..100 step 20) {
                emit(Progress.Extracting(i, "Extracting RootFS… $i%"))
                delay(100)
            }

            // Create a marker so we know install finished (real code would extract tarball here)
            File(rootfsDir, ".termix_installed").writeText(distro.id)

            // Also create a minimal fake /bin/sh so later PTY launch has something to talk to
            // until real RootFS is wired in.
            val bin = File(rootfsDir, "bin")
            bin.mkdirs()

            emit(Progress.Done(rootfsDir.absolutePath))
        } catch (e: Exception) {
            emit(Progress.Error(e.message ?: "Unknown error"))
        }
    }

    fun getRootfsDir(distroId: String): File {
        return File(context.filesDir, "rootfs/$distroId")
    }

    companion object {
        /** Path to the proot binary once you ship it in jniLibs / assets */
        fun prootBinary(context: Context): File {
            // Preferred: extracted from assets or nativeLibraryDir
            val fromNative = File(context.applicationInfo.nativeLibraryDir, "libproot.so")
            if (fromNative.exists()) return fromNative
            return File(context.filesDir, "bin/proot")
        }
    }
}
