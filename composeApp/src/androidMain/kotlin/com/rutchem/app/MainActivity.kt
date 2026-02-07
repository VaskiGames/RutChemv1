package com.rutchem.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rutchem.db.DriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tworzymy fabrykę (przekazujemy context)
        val driverFactory = DriverFactory(applicationContext)

        setContent {
            // Przekazujemy samą fabrykę do App.
            // App zajmie się tworzeniem Repozytorium i wywołaniem createDriver wewnątrz.
            App(driverFactory)
        }
    }
}