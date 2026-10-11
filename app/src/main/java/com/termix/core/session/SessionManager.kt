package com.termix.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Manages up to [MAX_SESSIONS] concurrent terminal sessions.
 */
class SessionManager {

    companion object {
        const val MAX_SESSIONS = 20
    }

    private val _sessions = MutableStateFlow<List<TerminalSession>>(emptyList())
    val sessions: StateFlow<List<TerminalSession>> = _sessions.asStateFlow()

    private val _activeSessionId = MutableStateFlow<String?>(null)
    val activeSessionId: StateFlow<String?> = _activeSessionId.asStateFlow()

    val activeSession: TerminalSession?
        get() = _sessions.value.find { it.id == _activeSessionId.value }

    val canCreateMore: Boolean
        get() = _sessions.value.size < MAX_SESSIONS

    /**
     * Create a new session. Returns null if limit reached.
     */
    fun createSession(title: String? = null): TerminalSession? {
        if (!canCreateMore) return null

        val index = _sessions.value.size + 1
        val session = TerminalSession(
            title = title ?: "Session $index"
        )

        _sessions.update { it + session }
        _activeSessionId.value = session.id
        return session
    }

    fun switchTo(sessionId: String) {
        if (_sessions.value.any { it.id == sessionId }) {
            _activeSessionId.value = sessionId
        }
    }

    fun closeSession(sessionId: String) {
        _sessions.update { list -> list.filterNot { it.id == sessionId } }

        // If we closed the active one, switch to another (or null)
        if (_activeSessionId.value == sessionId) {
            _activeSessionId.value = _sessions.value.lastOrNull()?.id
        }
    }

    fun renameSession(sessionId: String, newTitle: String) {
        _sessions.update { list ->
            list.map { if (it.id == sessionId) it.copy(title = newTitle) else it }
        }
    }

    fun ensureAtLeastOne(): TerminalSession {
        val current = activeSession
        if (current != null) return current
        return createSession() ?: error("Cannot create session")
    }
}
