package com.example.todo.data.repo

import com.example.todo.data.remote.TodoApi
import com.example.todo.data.remote.dto.CreateTodoRequest
import com.example.todo.data.remote.dto.TodoDto
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodosRepository @Inject constructor(
    private val api: TodoApi
) {
    suspend fun fetchTodos(): Result<List<TodoDto>> = try {
        Result.success(api.listTodos().todos)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: IOException){
        Result.failure(e)
    }

    suspend fun createTodo(title: String, description: String?): Result<TodoDto> = try {
        val body = CreateTodoRequest(title.trim(), description = description?.trim()?.ifBlank { null })
        Result.success(api.createTodo(body).todo)
    } catch (e: HttpException){
        Result.failure(e)
    } catch (e: IOException){
        Result.failure(e)
    }
}