package nz.ac.canterbury.seng303.flashcardapp.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import nz.ac.canterbury.seng303.flashcardapp.models.FlashCard
import nz.ac.canterbury.seng303.flashcardapp.models.Identifiable
import java.lang.reflect.Type

class PersistentStorage<T> (
    private val gson: Gson,
    private val type: Type,
    private val dataStore: DataStore<Preferences>,
    private val preferenceKey: Preferences.Key<String>
) : Storage <T> where T: Identifiable {

    private val flashCardListType = TypeToken.getParameterized(List::class.java, FlashCard::class.java).type


    override fun insert(data: T): Flow<Int> {
        return flow {
            val cachedDataClone = getAll().first().toMutableList()
            Log.d("PersistentStorage", "Inserting data: $data")
            cachedDataClone.add(data)
            dataStore.edit {
                val jsonString = gson.toJson(cachedDataClone, flashCardListType)
                Log.d("PersistentStorage", "JSON String after insert: $jsonString")
                it[preferenceKey] = jsonString
                emit(OPERATION_SUCCESS)
            }
        }
    }


    override fun insertAll(data: List<T>): Flow<Int> {
        return flow {
            val cachedDataClone = getAll().first().toMutableList()
            cachedDataClone.addAll(data)
            dataStore.edit {
                val jsonString = gson.toJson(cachedDataClone, flashCardListType)
                it[preferenceKey] = jsonString
                emit(OPERATION_SUCCESS)
            }
        }
    }

    override fun getAll(): Flow<List<T>> {
        return dataStore.data.map { preferences ->
            val jsonString = preferences[preferenceKey] ?: EMPTY_JSON_STRING
            Log.d("PersistentStorage", "Retrieved JSON: $jsonString")
            try {
                gson.fromJson(jsonString, flashCardListType)
            } catch (e: JsonSyntaxException) {
                Log.e("JsonError", "Error parsing JSON: $jsonString", e)
                emptyList()
            }
        }
    }




    override fun edit(identifier: Int, data: T): Flow<Int> {
        return flow {
            val cachedDataClone = getAll().first().toMutableList()
            val index = cachedDataClone.indexOfFirst { it.getIdentifier() == identifier }
            if (index != -1) {
                cachedDataClone[index] = data
                dataStore.edit {
                    val jsonString = gson.toJson(cachedDataClone, flashCardListType)
                    it[preferenceKey] = jsonString
                    emit(OPERATION_SUCCESS)
                }
            } else {
                emit(OPERATION_FAILURE)
            }
        }
    }

    override fun get(where: (T) -> Boolean): Flow<T> {
       return flow {
           val data = getAll().first().first(where)
           emit(data)
       }
    }

    override fun delete(identifier: Int): Flow<Int> {
        return flow {
            val cachedDataClone = getAll().first().toMutableList()
            val updatedData = cachedDataClone.filterNot { it.getIdentifier() == identifier }
            dataStore.edit {
                val jsonString = gson.toJson(updatedData, flashCardListType)
                it[preferenceKey] = jsonString
                emit(OPERATION_SUCCESS)
            }
        }
    }

    companion object {
        private const val OPERATION_SUCCESS = 1
        private const val OPERATION_FAILURE = -1
        private const val EMPTY_JSON_STRING = "[]"
    }

}