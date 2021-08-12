package se.umu.student.wili0037.geofeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.umu.student.wili0037.geofeed.repository.Repository

class MainViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}