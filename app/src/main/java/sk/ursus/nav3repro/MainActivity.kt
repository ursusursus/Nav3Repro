package sk.ursus.nav3repro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val backStack = rememberNavBackStack(HomeNavKey)
            NavDisplay(
                backStack = backStack,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                sceneStrategies = listOf(BottomSheetSceneStrategy()),
                entryProvider = entryProvider {
                    entry<HomeNavKey> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Home")
                            Button(onClick = { backStack.add(AboutNavKey1) }) {
                                Text("Go to about 1")
                            }
                        }
                    }
                    entry<AboutNavKey1>(
                        metadata = BottomSheetSceneStrategy.bottomSheet(),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("About 1")
                            Button(onClick = {
                                // backStack.removeLastOrNull()
                                backStack.add(AboutNavKey2)
                            }) {
                                Text("Go to about 2")
                            }
                            Button(onClick = {
                                // backStack.removeLastOrNull()
                                backStack.removeLastOrNull()
                            }) {
                                Text("Close")
                            }
                        }
                    }
                    entry<AboutNavKey2>(
                        metadata = BottomSheetSceneStrategy.bottomSheet(),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.Yellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("About 2")
                        }
                    }
                }
            )
        }
    }
}

interface AppNavKey : NavKey

@Serializable
data object HomeNavKey : AppNavKey

@Serializable
data object AboutNavKey1 : AppNavKey

@Serializable
data object AboutNavKey2 : AppNavKey
