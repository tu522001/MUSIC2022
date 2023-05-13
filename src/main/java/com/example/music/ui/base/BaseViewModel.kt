package com.example.music.ui.base

import androidx.lifecycle.ViewModel
import com.example.music.usecase.errors.ErrorManager
import javax.inject.Inject


/**
 * Created by TruyenIT
 */


abstract class BaseViewModel : ViewModel() {
    /**Inject Singleton ErrorManager
     * Use this errorManager to get the Errors
     */
    @Inject
    lateinit var errorManager: ErrorManager
}
