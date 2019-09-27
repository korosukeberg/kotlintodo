package com.github.korosukeberg.demotodo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FamilyService (private val familyRepository: FamilyRepository) {
    fun findAll() = familyRepository.findAll()
    fun findOne(id: Long) = familyRepository.findById(id)
}
