package com.family.happiness.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.family.happiness.HappinessApplication
import com.family.happiness.R
import com.family.happiness.adapter.FamilyListAdapter
import com.family.happiness.databinding.ActivityMainBinding
import com.family.happiness.databinding.NavHeaderBinding
import com.family.happiness.network.SafeResource
import com.family.happiness.ui.createfamily.CreateFamilyFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels() {
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
        }

        viewModel.joinFamilyEvent.observe(this){ event ->
            event.getContentIfNotHandled()?.let {
                val text = if(it is SafeResource.Success) "Join Successful" else "Join Failed"
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.leaveFamilyEvent.observe(this){ event ->
            event.getContentIfNotHandled()?.let {
                val text = if(it is SafeResource.Success) "Left Family" else "Leave Failed"
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClickSignOut() {
        viewModel.clearUserData()
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

    fun onClickLeaveFamily() {
        viewModel.leaveFamily()
    }

    fun onClickCopyFamilyCode() {
        viewModel.personalData.value?.familyId?.let { familyId ->
            ContextCompat.getSystemService(this, ClipboardManager::class.java)?.let {
                it.setPrimaryClip(ClipData.newPlainText("Family Code", familyId))
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }
}