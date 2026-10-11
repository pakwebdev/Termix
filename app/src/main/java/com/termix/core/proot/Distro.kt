package com.termix.core.proot

/**
 * Supported Linux distributions for PRoot.
 * RootFS is downloaded on first install (not bundled in APK).
 */
data class Distro(
    val id: String,
    val displayName: String,
    val version: String,
    val description: String,
    /** Approximate download size in MB (for UI only) */
    val sizeMb: Int,
    /** Placeholder URL — replace with real OCI/tarball URL when implementing installer */
    val rootfsUrl: String,
)

object DistroCatalog {
    val all: List<Distro> = listOf(
        Distro(
            id = "ubuntu-24.04",
            displayName = "Ubuntu",
            version = "24.04 LTS",
            description = "Recommended. Full-featured, huge package repos.",
            sizeMb = 80,
            rootfsUrl = "https://example.com/ubuntu-24.04-arm64.tar.xz"
        ),
        Distro(
            id = "debian-12",
            displayName = "Debian",
            version = "12 Bookworm",
            description = "Stable and minimal. Great for servers & scripts.",
            sizeMb = 70,
            rootfsUrl = "https://example.com/debian-12-arm64.tar.xz"
        ),
        Distro(
            id = "alpine-3.19",
            displayName = "Alpine",
            version = "3.19",
            description = "Tiny footprint. Uses musl + apk.",
            sizeMb = 8,
            rootfsUrl = "https://example.com/alpine-3.19-arm64.tar.gz"
        ),
        Distro(
            id = "arch",
            displayName = "Arch Linux",
            version = "Rolling",
            description = "Bleeding edge packages. For advanced users.",
            sizeMb = 90,
            rootfsUrl = "https://example.com/arch-arm64.tar.xz"
        ),
    )

    fun byId(id: String): Distro? = all.find { it.id == id }
}
