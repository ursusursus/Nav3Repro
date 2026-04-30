package sk.ursus.nav3repro


import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

/** An [SceneStrategy] that renders a [NavEntry] within a [ModalBottomSheet]. */
@OptIn(ExperimentalMaterial3Api::class)
class AnimatedBottomSheetSceneStrategy() : SceneStrategy<Any> {

    override fun SceneStrategyScope<Any>.calculateScene(entries: List<NavEntry<Any>>): Scene<Any>? {
        val entry = entries.lastOrNull() ?: return null
        entry.metadata[MetadataKey] ?: return null

        return object : OverlayScene<Any> {
            override val key = entry.contentKey
            override val entries = listOf(entry)
            override val previousEntries = entries.dropLast(1)
            override val overlaidEntries = entries.dropLast(1)

            lateinit var sheetState: SheetState

            override val content: @Composable (() -> Unit) = {
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                val minHeight = LocalWindowInfo.current.containerSize.height * 0.2 // 50% height
                ModalBottomSheet(
                    onDismissRequest = onBack,
                    sheetState = sheetState,
                    modifier = Modifier.heightIn(min = minHeight.dp),
                ) {
                    entry.Content()
                }
            }

            override suspend fun onRemove() {
                // run hide animations when this bottom sheet is popped from the backStack
                sheetState.hide()
            }
        }
    }

    companion object {
        object MetadataKey : NavMetadataKey<ModalBottomSheetProperties>

        fun bottomSheet(
            sheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties()
        ) = metadata { put(MetadataKey, sheetProperties) }
    }
}
