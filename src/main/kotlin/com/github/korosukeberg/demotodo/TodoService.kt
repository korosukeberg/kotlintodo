package com.github.korosukeberg.demotodo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TodoService(private val todoRepository: TodoRepository) {
    fun findAll() = todoRepository.findAll()
    fun save(todo: ToDo) = todoRepository.save(todo)
    fun delete(ids: List<Long>) = todoRepository.deleteByIdIn(ids)
    fun markCompleted(id: Long, todo: ToDo) {
        var todoToUpdate: ToDo = todoRepository.getOne(id)
        todoToUpdate.completed = todo.completed
        todoRepository.save(todoToUpdate)
    }
}