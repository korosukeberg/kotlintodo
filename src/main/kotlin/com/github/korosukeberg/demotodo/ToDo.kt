package com.github.korosukeberg.demotodo

import javax.persistence.*

@Entity
@Table(name="todo")
data class ToDo(
        @Id
        @GeneratedValue
        val id: Long = -1,
        val title: String = "",
        @Column(name="iscompleted")
        val isCompleted: Boolean = false
) {}
