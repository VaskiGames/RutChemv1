package com.rutchem.app

import androidx.compose.ui.window.ComposeUIViewController
import com.rutchem.db.DriverFactory

fun MainViewController() = ComposeUIViewController {
    val driverFactory = DriverFactory()
    App(driverFactory)}