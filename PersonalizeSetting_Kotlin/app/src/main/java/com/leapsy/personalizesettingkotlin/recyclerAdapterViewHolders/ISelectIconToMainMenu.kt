package com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders

import com.leapsy.personalizesettingkotlin.menuHelper.InstalledItems
import java.util.ArrayList

interface ISelectIconToMainMenu {
    fun onSelectedIcon(aSelectedIconList : ArrayList<InstalledItems>, aSelectedOrder : Int)
}