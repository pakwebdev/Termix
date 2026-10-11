package com.termix.core.session

import java.util.UUID

/**
 * One interactive terminal session (max 20 total).
 * Later this will hold a real PTY file descriptor / process handle.
 */
data class TerminalSession(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    /** Last known working directory inside the guest */
    var cwd: String = "/root",
    /** Whether the backing process is still alive */
    var isAlive: Boolean = true,
)
