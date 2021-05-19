package com.family.happiness.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.family.happiness.Event
import com.family.happiness.HappinessApplication
import com.family.happiness.R
import com.family.happiness.adapter.FamilyListAdapter
import com.family.happiness.databinding.ActivityMainBinding
import com.family.happiness.databinding.NavHeaderBinding
import com.family.happiness.network.SafeResource
import com.family.happiness.ui.createfamily.CreateFamilyFragmentDirections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainActivityViewModel: MainActivityViewModel by viewModels() {
        ViewModelFactory(application as HappinessApplication)
    }
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
//        findNavController(R.id.navHostFragment)
    }

    private val homeSet = setOf(R.id.mailFragment, R.id.albumFragment, R.id.wishesFragment)
    private val noToolBarSet = setOf(R.id.signInFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            viewModel = mainActivityViewModel
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
        mainActivityViewModel.personalData.observe(this) {
            if (it.token == null) {
                navController.navigate(R.id.action_global_signInFragment)
            }
        }

        mainActivityViewModel.joinFamilyEvent.observe(this){ event ->
            event.getContentIfNotHandled()?.let {
                val text = if(it is SafeResource.Success) "Join Successful" else "Join Failed"
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }

        mainActivityViewModel.leaveFamilyEvent.observe(this){ event ->
            event.getContentIfNotHandled()?.let {
                val text = if(it is SafeResource.Success) "Left Family" else "Leave Failed"
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClickSignOut() {
        mainActivityViewModel.clearUserData()
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
                mainActivityViewModel.joinFamily(input.text.toString())
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    fun onClickLeaveFamily() {
        mainActivityViewModel.leaveFamily()
    }

    fun onClickCopyFamilyCode() {
        mainActivityViewModel.personalData.value?.familyId?.let { familyId ->
            ContextCompat.getSystemService(this, ClipboardManager::class.java)?.let {
                it.setPrimaryClip(ClipData.newPlainText("Family Code", familyId))
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }
}