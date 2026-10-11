package com.termix.app

import android.app.Application

class TermixApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Future: init proot paths, session store, etc.
    }
}
