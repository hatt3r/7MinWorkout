package com.cessabit.a7minworkoutapplication

import android.app.Application

class WorkOutApp : Application() {
    val db by lazy {
        HistoryDatabase.getInstance(this)
    }
}