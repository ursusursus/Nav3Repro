package sk.ursus.nav3repro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.saved
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private val backStack by saved(NavBackStackSerializer(NavKeySerializer())) {
        NavBackStack<AppNavKey>(HomeNavKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NavDisplay(
                backStack = backStack,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeLastOrNull()
                    }
                },
                transitionSpec = pushSlideAndFadeTransitionSpec(),
                popTransitionSpec = popSlideAndFadeTransitionSpec(),
                entryProvider = entryProvider {
                    entry<HomeNavKey> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Home")
                        }
                    }
                    entry<ProfileNavKey> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Yellow),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Profile")
                        }
                    }
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        val uri = intent.data
        if (uri != null) {
            if (uri.pathSegments.firstOrNull() == "profile") {
                backStack.add(ProfileNavKey)
            }
        }
    }
}

interface AppNavKey : NavKey

@Serializable
data object HomeNavKey : AppNavKey

@Serializable
data object ProfileNavKey : AppNavKey

fun <T : Any> pushSlideAndFadeTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(durationMillis = 300)
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth / 4 },
        animationSpec = tween(durationMillis = 300)
    ) + fadeOut(
        animationSpec = tween(durationMillis = 300)
    )
}

fun <T : Any> popSlideAndFadeTransitionSpec(): AnimatedContentTransitionScope<Scene<T>>.() -> ContentTransform = {
    slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth / 4 },
        animationSpec = tween(durationMillis = 300)
    ) + fadeIn(
        animationSpec = tween(durationMillis = 300)
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth / 2 },
        animationSpec = tween(durationMillis = 300)
    ) + fadeOut(
        animationSpec = tween(durationMillis = 300)
    )
}
