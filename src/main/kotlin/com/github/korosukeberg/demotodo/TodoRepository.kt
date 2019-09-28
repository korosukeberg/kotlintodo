package com.github.korosukeberg.demotodo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository: JpaRepository<ToDo, Long> {
    fun deleteByIdIn(ids: List<Long>);
}
