package com.github.korosukeberg.demotodo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FamilyRepository: JpaRepository<Family, Long> {}
