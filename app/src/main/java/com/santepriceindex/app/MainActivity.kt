package com.santepriceindex.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.santepriceindex.app.ui.navigation.SanteNavGraph
import com.santepriceindex.app.ui.theme.SantePriceTheme
import com.santepriceindex.app.ui.viewmodel.SanteAppViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val appViewModel: SanteAppViewModel by viewModels()
    private var signInError by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SantePriceTheme {
                SanteNavGraph(
                    viewModel = appViewModel,
                    signInError = signInError,
                    onGoogleSignIn = ::startGoogleSignIn
                )
            }
        }
    }

    private fun startGoogleSignIn() {
        val webClientId = getString(R.string.google_web_client_id)
        if (webClientId.startsWith("YOUR_WEB_CLIENT_ID")) {
            signInError = "Preview mode: add your Google web client ID for production sign-in."
            appViewModel.signIn("google.preview@sante.local", "Google Preview")
            return
        }

        lifecycleScope.launch {
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = CredentialManager.create(this@MainActivity)
                    .getCredential(this@MainActivity, request)
                val credential = result.credential

                if (
                    credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    appViewModel.signIn(
                        email = googleCredential.id,
                        displayName = googleCredential.displayName ?: googleCredential.id
                    )
                    signInError = null
                } else {
                    signInError = "Google sign-in was not completed. Please try again."
                }
            } catch (exception: Exception) {
                signInError = exception.localizedMessage ?: "Google sign-in failed. Please try again."
            }
        }
    }
}
