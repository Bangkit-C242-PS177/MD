package com.example.urkins.ui.main.user

import androidx.lifecycle.ViewModel
import com.example.urkins.data.local.entity.HistoryEntity
import com.example.urkins.data.repository.HistoryRepository

class UserViewModel2 (
    private val historyRepository: HistoryRepository
) : ViewModel() {

    fun getHistory()= historyRepository.showHistory()
    fun deleteHistory(historyEntity: HistoryEntity) {
        historyRepository.deleteHistory(historyEntity)
    }
}