package se.umu.edmo0011.discgolftracker.composables.measuring

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import se.umu.edmo0011.discgolftracker.MeasureGraph
import se.umu.edmo0011.discgolftracker.viewModels.MeasureViewModel
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.composables.general.BigButton
import se.umu.edmo0011.discgolftracker.composables.general.DismissableAlert
import se.umu.edmo0011.discgolftracker.composables.general.createPermissionLauncher
import se.umu.edmo0011.discgolftracker.sharedViewModel

@Composable
fun StartMeasureScreen(navCon: NavController, scafState: ScaffoldState)
{
    scafState.topBar?.navAction = {navCon.navigateUp()}
    val context = navCon.context
    val model = navCon.currentBackStackEntry?.sharedViewModel<MeasureViewModel>(navCon, MeasureGraph.route) ?: return
    StartMeasureAlerts(model = model)

    val launcher = createPermissionLauncher(
        onGranted = { model.startGps(context, navCon,{}) },
        onNotGranted = { model.onNotGrantedPermission() }
    )

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(id = R.string.start_measure_text), style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.size(30.dp))
        Box {
            if(!model.lookingForGps)
                BigButton(text = stringResource(id = R.string.start_measure_button),
                    textStyle = MaterialTheme.typography.displayMedium) {
                    model.startGps(context, navCon,
                        onNoPermission = {launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)})
                }
                /*Button(
                    onClick = {
                        model.startGps(context, navCon,
                            onNoPermission = {launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)})
                    }
                ){
                    Text(text = stringResource(id = R.string.start_measure_button))
                }*/
            else
                CircularProgressIndicator()
        }
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

