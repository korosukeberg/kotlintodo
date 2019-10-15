package com.github.korosukeberg.demotodo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoService(private val todoRepository: TodoRepository) {
    fun findAll() = todoRepository.findAllByOrderByIdAsc()
    fun save(todo: ToDo) = todoRepository.save(todo)
    fun delete(id: Long) = todoRepository.deleteById(id)
    fun deleteCompleted() = todoRepository.deleteCompleted()
    fun deleteAll() = todoRepository.deleteAll()
    fun toggleCompleted(id: Long, todo: ToDo) {
        var todoToUpdate: ToDo = todoRepository.getOne(id)
        todoToUpdate.completed = todo.completed
        todoRepository.save(todoToUpdate)
    }
}
