<template>
  <v-app>
    <v-container id="container">
      <v-img :src="require('./assets/logo.svg')" class="my-3" contain height="200"></v-img>
      <todo-text-input></todo-text-input>
      <todo-subactions @loadToDos="load"></todo-subactions>
      <todo-list></todo-list>
    </v-container>

    <v-snackbar v-model="this.snackbar.show" :color="this.snackbar.color" :bottom="true" :right="true" :timeout=6000>
      {{ this.snackbar.text }}
      <v-btn dark text @click="this.snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </v-app>
</template>

<script>
  import axios from "./plugins/axiosbase";
  import TodoTextInput from "./components/todo-text-input"
  import TodoList from "./components/todo-list"
  import TodoSubactions from "./components/todo-subactions"
  import {mapMutations, mapState} from "vuex";

  export default {
    name: 'App',
    components: {
      TodoTextInput,
      TodoSubactions,
      TodoList
    },
    data: function () {
      return {}
    },
    mounted: function () {
      this.load();
    },
    computed: {
      ...mapState(["items", "snackbar"])
    },
    methods: {
      ...mapMutations(["clearItems", "showNoTodoSnackbar", "showErrorSnackbar"]),
      load: function () {
        axios.get("/todo").then(response => {
          if (!response.data.length) {
            this.showNoTodoSnackbar();
            return;
          }
          for (let i = 0; i < response.data.length; i++) {
            this.items.push(response.data[i])
          }
        })
      }
    }
  }
</script>
<style lang="scss">
  #container {
    max-width: 550px;
  }

  .done {
    text-decoration: line-through;
  }

  .v-list-item:hover {
    .clear-btn {
      display: block !important;
    }
  }
</style>
