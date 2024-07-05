package com.easyapps.jetutilssample

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import com.easyapps.jetutilssample.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JetUtilsSampleTheme {

            }
        }
    }
}