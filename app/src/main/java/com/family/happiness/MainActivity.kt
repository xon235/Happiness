package com.family.happiness

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.text.InputType
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
import com.family.happiness.databinding.ActivityMainBinding
import com.family.happiness.databinding.NavHeaderBinding
import com.family.happiness.fragments.CreateFamilyFragmentDirections
import com.family.happiness.viewmodels.HappinessViewModel
import com.family.happiness.viewmodels.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private var signedIn = false

    private val happinessViewModel: HappinessViewModel by viewModels(){
        ViewModelFactory((application as HappinessApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.navigationView
        val navViewBinding: NavHeaderBinding = NavHeaderBinding.bind(navView.getHeaderView(0))

        navViewBinding.mainActivity = this
        navViewBinding.lifecycleOwner = this
        navViewBinding.viewModel = happinessViewModel
        navViewBinding.recyclerView.adapter = FamilyListAdapter()

        val bottomNavigationView = binding.bottomNavigationView
        navController = (supportFragmentManager
                .findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
        bottomNavigationView.setupWithNavController(navController)

        val toolbar = binding.toolbar
        val drawerLayout = binding.drawerLayout
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(
            navController, AppBarConfiguration(
                setOf(R.id.mailFragment, R.id.albumFragment, R.id.wishesFragment),
                drawerLayout = drawerLayout,
            )
        )

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if(destination.id in setOf(R.id.mailFragment, R.id.albumFragment, R.id.wishesFragment)){
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                bottomNavigationView.visibility = View.VISIBLE
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                bottomNavigationView.visibility = View.GONE
            }

            if(destination.id == R.id.signInFragment) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
            }
        }

        happinessViewModel.user.observe(this) {
            if (it == null) {
                navController.navigate(R.id.action_global_signInFragment)
                signedIn = false
            } else if(!signedIn){
//                happinessViewModel.silentSignIn()
                signedIn = true
            } else {
//                happinessViewModel.updateMembers()
            }
        }
    }

    fun onClickCreateFamily(){
        navController.navigate(CreateFamilyFragmentDirections.actionGlobalCreateFamilyFragment())
    }

    fun onClickJoinFamily(){
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle("Enter Family Code")
            .setView(input)
            .setPositiveButton("Join") {
                    _, _ -> happinessViewModel.joinFamily(input.text.toString(), this)
            }
            .setNegativeButton("Cancel") {
                    dialog, _ -> dialog.cancel()
            }
            .show()
    }

    fun onClickCopyFamilyCode(){
        val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("Family Code", happinessViewModel.user.value!!.id_family)
        clipboard!!.setPrimaryClip(clip)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    fun onClickLeaveFamily(){
        happinessViewModel.leaveFamily(this)
    }
}