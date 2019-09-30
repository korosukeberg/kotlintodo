package com.github.korosukeberg.demotodo

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.Collections
import javax.servlet.Filter

@Controller
@RequestMapping
class ToDoController(private val familyService: FamilyService, private val todoService: TodoService) {

    @GetMapping("/family")
    @ResponseBody
    fun getAllFamilies(): List<Family> {
        return familyService.findAll()
    }

    @GetMapping("/todo")
    @ResponseBody
    fun getTodos(): List<ToDo> {
        return todoService.findAll()
    }

    @PostMapping("/todo")
    fun saveToDo(@RequestBody todo: ToDo): ToDo {
        return todoService.save(todo)
    }

    @PostMapping("/todo/{id}")
    fun markCompleted(@PathVariable(value = "id") id: Long, @RequestBody todo: ToDo) {
        todoService.markCompleted(id, todo)
    }

    @DeleteMapping("/todo")
    fun deleteToDo(@RequestBody ids: List<Long>) {
        todoService.delete(ids)
    }

    @RequestMapping("/")
    fun getTodoHtml(): String {
        return "todo"
    }

    @RequestMapping("/index")
    fun getIndexHtml(): String {
        return "index"
    }

    @Bean
    fun simpleCorsFilter(): FilterRegistrationBean<*> {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        // *** URL below needs to match the Vue client URL and port ***
        config.allowedOrigins = Collections.singletonList("http://localhost:8080")
        config.allowedMethods = Collections.singletonList("*")
        config.allowedHeaders = Collections.singletonList("*")
        source.registerCorsConfiguration("/**", config)
        val bean = FilterRegistrationBean<Filter>(CorsFilter(source))
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE)
        return bean
    }
}
