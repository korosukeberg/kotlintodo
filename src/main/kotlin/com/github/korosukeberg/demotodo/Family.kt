package com.github.korosukeberg.demotodo

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Family(
        @Id
        @GeneratedValue
        val no: Long? = null,
        val attr: String? = null,
        val name: String? = null,
        val age: Int? = null
) {}
