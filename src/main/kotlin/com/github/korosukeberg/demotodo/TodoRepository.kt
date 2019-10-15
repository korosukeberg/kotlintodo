package com.github.korosukeberg.demotodo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository : JpaRepository<ToDo, Long> {
    fun findAllByOrderByIdAsc(): List<ToDo>
    @Modifying
    @Query("delete from ToDo t where t.completed = true")
    fun deleteCompleted()
}
