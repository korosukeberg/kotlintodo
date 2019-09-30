package com.github.korosukeberg.demotodo

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "todo")
data class ToDo(
    @Id
    var id: Long = -1,
    var title: String = "",
    var completed: Boolean = false
) {}
