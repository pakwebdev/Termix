package com.termix.ui.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.termix.core.session.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(
    sessionManager: SessionManager = remember { SessionManager() }
) {
    val sessions by sessionManager.sessions.collectAsState()
    val activeId by sessionManager.activeSessionId.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showSessionMenu by remember { mutableStateOf(false) }
    var showExtraKeys by remember { mutableStateOf(true) }

    // Ensure at least one session exists
    LaunchedEffect(Unit) {
        sessionManager.ensureAtLeastOne()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Terminal,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Termix",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    // Session count + menu
                    Box {
                        IconButton(onClick = { showSessionMenu = true }) {
                            Text(
                                text = "${sessions.size}/${SessionManager.MAX_SESSIONS}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        DropdownMenu(
                            expanded = showSessionMenu,
                            onDismissRequest = { showSessionMenu = false }
                        ) {
                            sessions.forEach { session ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            session.title + if (session.id == activeId) " ●" else ""
                                        )
                                    },
                                    onClick = {
                                        sessionManager.switchTo(session.id)
                                        showSessionMenu = false
                                    },
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            sessionManager.closeSession(session.id)
                                        }) {
                                            Icon(Icons.Default.Close, contentDescription = "Close")
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // New session
                    IconButton(onClick = {
                        val created = sessionManager.createSession()
                        if (created == null) {
                            scope.launch {
                                snackbar.showSnackbar("Maximum 20 sessions reached")
                            }
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "New session")
                    }

                    // Toggle extra keys
                    IconButton(onClick = { showExtraKeys = !showExtraKeys }) {
                        Icon(Icons.Default.Keyboard, contentDescription = "Extra keys")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Session tabs (horizontal)
            if (sessions.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sessions.forEach { session ->
                        FilterChip(
                            selected = session.id == activeId,
                            onClick = { sessionManager.switchTo(session.id) },
                            label = { Text(session.title, maxLines = 1) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(0.dp)
                                )
                            }
                        )
                    }
                }
            }

            // Terminal output area (placeholder until real TerminalView is wired)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFF0D1117))
                    .padding(12.dp)
            ) {
                val active = sessions.find { it.id == activeId }
                val scroll = rememberScrollState()

                Column(modifier = Modifier.verticalScroll(scroll)) {
                    Text(
                        text = buildString {
                            appendLine("Termix v0.1.0")
                            appendLine("────────────────────────────")
                            if (active != null) {
                                appendLine("Session : ${active.title}")
                                appendLine("CWD     : ${active.cwd}")
                                appendLine()
                                appendLine("PTY backend not wired yet.")
                                appendLine("Next steps:")
                                appendLine("  1. Drop libproot.so into jniLibs/arm64-v8a/")
                                appendLine("  2. Implement real RootFS download in ProotInstaller")
                                appendLine("  3. Hook Termux terminal-view or native PTY")
                                appendLine()
                                append("root@termix:~# ")
                            } else {
                                appendLine("No active session.")
                            }
                        },
                        color = Color(0xFF3FB950),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            // Termux-style extra keys row
            if (showExtraKeys) {
                ExtraKeysBar(
                    onKey = { key ->
                        // Later: send escape sequences / control chars to active PTY
                    }
                )
            }
        }
    }
}

@Composable
private fun ExtraKeysBar(onKey: (String) -> Unit) {
    val keys = listOf(
        "ESC", "CTRL", "ALT", "TAB",
        "↑", "↓", "←", "→",
        "-", "/", "|", "HOME", "END"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        keys.forEach { key ->
            androidx.compose.material3.Surface(
                onClick = { onKey(key) },
                shape = RoundedCornerShape(6.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.height(36.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = key,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
