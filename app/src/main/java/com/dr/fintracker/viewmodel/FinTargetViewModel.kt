package com.dr.fintracker.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dr.fintracker.data.db.fintarget.FinTargetRepository
import com.dr.fintracker.data.entities.FinTarget
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FinTargetViewModel @ViewModelInject constructor(
    private val repository: FinTargetRepository
) : ViewModel() {

    fun getAllTargets() = repository.getFinTargets()

    fun insert(target: FinTarget) =
        GlobalScope.launch {
            repository.insert(target)
        }

    fun update(target: FinTarget) =
        GlobalScope.launch {
            repository.update(target)
        }

    fun delete(target: FinTarget) =
        GlobalScope.launch {
            repository.delete(target)
        }
}