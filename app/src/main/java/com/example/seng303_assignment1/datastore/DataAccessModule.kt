package com.example.seng303_assignment1.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.seng303_assignment1.model.FlashCard
import com.example.seng303_assignment1.viewModels.EditFlashCardViewModel
import com.example.seng303_assignment1.viewModels.FlashCardViewModel
import com.example.seng303_assignment1.viewModels.NewFlashCardViewModel
import com.example.seng303_assignment1.viewModels.PlayViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "flashcard_data")

@FlowPreview
val dataAccessModule = module {
    single<Storage<FlashCard>> {
        PersistentStorage(
            gson = get(),
            type = object: TypeToken<List<FlashCard>>(){}.type,
            preferenceKey = stringPreferencesKey("notes"),
            dataStore = androidContext().dataStore
        )
    }

    single { Gson() }

    viewModel {
        FlashCardViewModel(
            flashCardStorage = get()
        )
    }
    viewModel {
        NewFlashCardViewModel()
    }
    viewModel {
        EditFlashCardViewModel()
    }
    viewModel {
        PlayViewModel()
    }
}
