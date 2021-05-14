package com.family.happiness.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.family.happiness.HappinessApplication

abstract class HappinessBaseFragment: Fragment() {

    protected val happinessViewModel: HappinessViewModel by activityViewModels(){
        ViewModelFactory((requireActivity().application as HappinessApplication).repository)
    }
}