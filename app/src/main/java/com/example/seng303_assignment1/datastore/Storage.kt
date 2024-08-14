package com.example.seng303_assignment1.datastore

import kotlinx.coroutines.flow.Flow
import com.example.seng303_assignment1.model.Identifiable

interface Storage<T> where T : Identifiable {
    fun insert(data: T): Flow<Int>
    fun insertAll(data: List<T>): Flow<Int>
    fun getAll(): Flow<List<T>>
    fun delete(identifier: Int): Flow<Int>
    fun edit(identifier: Int, data: T): Flow<Int>
    fun get(where: (T) -> Boolean): Flow<T>
}
