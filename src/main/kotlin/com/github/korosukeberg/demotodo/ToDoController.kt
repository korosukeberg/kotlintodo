package com.github.korosukeberg.demotodo

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping
class ToDoController (private val familyService: FamilyService, private val todoService: TodoService) {

    @GetMapping("/family")
    @ResponseBody
    fun getAllFamilies() :List<Family> {
        return familyService.findAll()
    }

    @GetMapping("/todo")
    @ResponseBody
    fun getTodos(): List<ToDo> {
        return todoService.findAll()
    }

    @RequestMapping("/")
    fun getTodoHtml() :String {
        return "todo"
    }

    @RequestMapping("/index")
    fun getIndexHtml() :String {
        return "index"
    }
}
