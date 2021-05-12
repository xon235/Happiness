package com.family.happiness.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.family.happiness.HappinessApplication
import com.family.happiness.viewmodels.HappinessViewModel
import com.family.happiness.viewmodels.ViewModelFactory

abstract class HappinessFragment: Fragment() {

    protected val happinessViewModel: HappinessViewModel by activityViewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }
}