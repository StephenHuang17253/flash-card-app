package nz.ac.canterbury.seng303.flashcardapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.FlowPreview
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.FlashCardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cards_data")

@FlowPreview
val dataAccessModule = module {
    single<Storage<FlashCard>> {
        PersistentStorage(
            gson = get(),
            type = object: TypeToken<List<FlashCard>>(){}.type,
            preferenceKey = stringPreferencesKey("flashcards"),
            dataStore = androidContext().dataStore
        )
    }

    single { Gson() }

    viewModel {
        FlashCardViewModel(
            flashCardStorage = get()
        )
    }

}