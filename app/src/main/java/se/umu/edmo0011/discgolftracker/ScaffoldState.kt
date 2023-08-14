package se.umu.edmo0011.discgolftracker

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class ScaffoldState(
    val topBar: TopBarState? = null,
    val fab: FabState? = null
)


data class TopBarState(
    @StringRes val title: Int? = null,
    val topNavIcon: ImageVector? = null,
    var navAction: (()->Unit)? = null,
    val actionIcons: List<ImageVector> = emptyList(),
    val actions: MutableList<()->Unit> = mutableListOf()
)


data class FabState(
    val fabIcon: ImageVector? = null,
    var fabAction: (()->Unit)? = null
)