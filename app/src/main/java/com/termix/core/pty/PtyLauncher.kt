package com.termix.core.pty

import android.content.Context
import com.termix.core.proot.ProotInstaller
import java.io.File

/**
 * Builds the command line that starts a shell inside PRoot.
 *
 * Real PTY wiring (JNI openpty / Termux terminal-emulator) comes later.
 * For now this only prepares the argv that will be handed to the PTY backend.
 */
object PtyLauncher {

    /**
     * Returns argv for:
     *   proot -r <rootfs> -0 --link2symlink -b /sdcard:/sdcard -w /root /bin/bash -l
     */
    fun buildProotShellCommand(
        context: Context,
        rootfsPath: String,
        shell: String = "/bin/bash",
        cwd: String = "/root",
    ): Array<String> {
        val proot = ProotInstaller.prootBinary(context).absolutePath

        return arrayOf(
            proot,
            "-r", rootfsPath,
            "-0",                       // fake root uid
            "--link2symlink",
            "-b", "/sdcard:/sdcard",
            "-b", "/data:/data",
            "-w", cwd,
            shell,
            "-l"
        )
    }

    /**
     * Fallback when proot binary is not yet present — just show a placeholder shell.
     */
    fun buildPlaceholderCommand(): Array<String> {
        return arrayOf("/system/bin/sh", "-c", "echo 'Termix: PRoot binary not installed yet'; echo 'Place libproot.so in jniLibs/arm64-v8a/'; exec /system/bin/sh")
    }

    fun hasProotBinary(context: Context): Boolean {
        val f = ProotInstaller.prootBinary(context)
        return f.exists() && f.canExecute()
    }
}
