package se.umu.edmo0011.discgolftracker.composables.measuring

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.viewModels.MeasureViewModel
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState
import se.umu.edmo0011.discgolftracker.composables.general.BigButton
import se.umu.edmo0011.discgolftracker.composables.general.DismissableAlert
import se.umu.edmo0011.discgolftracker.composables.general.createPermissionLauncher
import se.umu.edmo0011.discgolftracker.graphs.MeasureGraph
import se.umu.edmo0011.discgolftracker.misc.sharedViewModel

@Composable
fun StartMeasureScreen(navCon: NavController, scafState: ScaffoldState)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}
    val context = navCon.context
    val model = navCon.currentBackStackEntry?.sharedViewModel<MeasureViewModel>(navCon, MeasureGraph.route) ?: return
    StartMeasureAlerts(model = model)

    //Launcher used to ask for gps permission.
    val launcher = createPermissionLauncher(
        onGranted = { model.startGps(context, navCon,{}) },
        onNotGranted = { model.onNotGrantedPermission() }
    )

    StartMeasureScreenContent(lookingForGps = model.lookingForGps) {
        model.startGps(navCon.context, navCon){
            //On no gps permission. If we don't have gps permission, ask for it.
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

@Composable
fun StartMeasureScreenContent(lookingForGps: Boolean, onStart: ()->Unit)
{
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = stringResource(id = R.string.start_measure_text),
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.size(30.dp))

        if(!lookingForGps)
            BigButton(text = stringResource(id = R.string.start_measure_button),
                textStyle = MaterialTheme.typography.displayMedium) {
                onStart.invoke()
            }

        else
            CircularProgressIndicator()
    }
}

@Composable
fun StartMeasureAlerts(model: MeasureViewModel)
{
    DismissableAlert(
        show = model.showEnableAlert,
        title = R.string.title_gps_off,
        onDismiss = model::onDismissEnableAlert,
        dismissText = R.string.ok,
        text = R.string.text_gps_off
    )

    DismissableAlert(
        show = model.showPermissionNeededAlert,
        title = R.string.title_gps_not_granted,
        onDismiss = model::onDismissPermissionAlert,
        dismissText = R.string.ok,
        text = R.string.text_gps_not_granted
    )
    DismissableAlert(
        show = model.showFailedAlert,
        title = R.string.title_no_gps_signal,
        onDismiss = model::onDismissFailedAlert,
        dismissText = R.string.ok,
        text = R.string.text_no_gps_signal
    )
}

