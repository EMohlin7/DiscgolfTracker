package se.umu.edmo0011.discgolftracker.composables.general

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun createPermissionLauncher(onGranted: ()->Unit, onNotGranted: ()->Unit): ManagedActivityResultLauncher<String, Boolean>
{
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){granted->
        if(granted)
            onGranted.invoke()
        else
            onNotGranted.invoke()
    }
    return launcher
}