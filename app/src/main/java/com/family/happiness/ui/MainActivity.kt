package com.family.happiness.ui

import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.family.happiness.HappinessApplication
import com.family.happiness.R
import com.family.happiness.Utils
import com.family.happiness.adapter.FamilyListAdapter
import com.family.happiness.dataStore
import com.family.happiness.databinding.ActivityMainBinding
import com.family.happiness.databinding.NavHeaderBinding
import com.family.happiness.ui.createfamily.CreateFamilyFragmentDirections
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    companion object {
        private const val RESOLVE_GOOGLE_API_AVAILABILITY_SERVICES_ERROR = 102
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels() {
        ViewModelFactory(application as HappinessApplication)
    }
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
    }

    private val homeSet = setOf(R.id.mailFragment, R.id.albumFragment, R.id.wishFragment)
    private val noToolBarSet = setOf(R.id.splashFragment, R.id.signInFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pre-Load DataStore
        lifecycleScope.launch {
            applicationContext.dataStore.data.first()
        }

        // Setup Binding and ContentView
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(
            navController, AppBarConfiguration(homeSet, binding.drawerLayout)
        )

        // Setup NavigationView
        NavHeaderBinding.bind(binding.navigationView.getHeaderView(0)).apply {
            mainActivity = this@MainActivity
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
            recyclerView.adapter = FamilyListAdapter()
        }

        // Setup Bottom Navigation
        binding.bottomNavigationView.setupWithNavController(navController)

        // Setup NavController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isHome = destination.id in homeSet
            val isSignIn = destination.id in noToolBarSet

            binding.drawerLayout.setDrawerLockMode(
                if (isHome) DrawerLayout.LOCK_MODE_UNLOCKED
                else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            )
            binding.bottomNavigationView.visibility = if (isHome) View.VISIBLE else View.GONE
            binding.toolbar.visibility = if (isSignIn) View.GONE else View.VISIBLE
        }

        // Observe
        // Check authentication
        viewModel.personalData.observe(this) {
            if (it.token == null) {
                navController.navigate(R.id.action_global_signInFragment)
            }

            if (it.familyId != null) {
                viewModel.syncUser()
            }
        }

        viewModel.joinFamilyFlag.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                val text = if (it) "Join Successful" else "Join Failed"
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.leaveFamilyFlag.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                val text = if (it) "Left Family" else "Leave Failed"
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }

        // Check Google Play Services for FCM
        isGooglePlayServicesAvailable()
    }

    override fun onResume() {
        isGooglePlayServicesAvailable()
        cancelAllNotification()
        super.onResume()
    }

    fun cancelAllNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    fun onClickSignOut() {
        viewModel.clearUserData()
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build().also {
            GoogleSignIn.getClient(this, it).signOut()
        }
    }


    fun onClickCreateFamily() {
        navController.navigate(CreateFamilyFragmentDirections.actionGlobalCreateFamilyFragment())
    }

    fun onClickJoinFamily() {
        val input = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Enter Family Code")
            .setView(input)
            .setPositiveButton("Join") { _, _ ->
                viewModel.joinFamily(input.text.toString())
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    fun onClickReadQrCode() {
        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setPrompt("Scan a barcode or QR Code")
        intentIntegrator.setOrientationLocked(false)
        intentIntegrator.initiateScan()
    }

    fun onClickLeaveFamily() {
        viewModel.leaveFamily()
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(
                    this,
                    status,
                    RESOLVE_GOOGLE_API_AVAILABILITY_SERVICES_ERROR
                ).show()
            }
            return false
        }
        return true
    }

    fun onClickCopyFamilyCode() {
        viewModel.personalData.value?.familyId?.let { familyId ->
            ContextCompat.getSystemService(this, ClipboardManager::class.java)?.let {
                it.setPrimaryClip(ClipData.newPlainText("Family Code", familyId))
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClickFamilyQrCode() {
        val dialog = AlertDialog.Builder(this)
            .setPositiveButton("Done") { dialog, _ -> dialog.dismiss() }
            .create()

        dialog.setView(
            ImageView(this).apply{ setImageBitmap(Utils.encodeAsBitmap(viewModel.personalData.value?.familyId)) }
        )
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.joinFamily(intentResult.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}