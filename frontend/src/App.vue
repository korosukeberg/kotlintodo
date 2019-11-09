<template>
  <v-app>
    <v-container id="container">
      <v-img :src="require('./assets/logo.svg')" class="my-3" contain height="200"></v-img>
      <todo-text-input></todo-text-input>

      <v-layout justify-end v-show="this.items.length">
        <v-btn text small color="warning" @click="clearCompleted">CLEAR COMPLETED</v-btn>
        <v-btn text small color="error" @click="openClearAllDialog">CLEAR ALL</v-btn>
        <v-dialog v-model="clearAllDialog" max-width="290">
          <v-card>
            <v-card-title>Are you sure you want to clear all your todo?</v-card-title>
            <v-card-actions>
              <div class="flex-grow-1"></div>
              <v-btn color="blue darken-1" text @click="clearAllDialog = false">CANCEL</v-btn>
              <v-btn color="blue darken-1" text @click="clearAll">OK</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-layout>

      <todo-list></todo-list>

    </v-container>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" :bottom="true" :right="true" :timeout=6000>
      {{ snackbar.text }}
      <v-btn dark text @click="snackbar.show = false">Close</v-btn>
    </v-snackbar>
  </v-app>
</template>

<script>
  import axios from "./plugins/axiosbase";
  import TodoTextInput from "./components/todo-text-input"
  import TodoList from "./components/todo-list"
  import {mapMutations, mapState} from "vuex";

  export default {
    name: 'App',
    components: {
      TodoTextInput,
      TodoList
    },
    data: function () {
      return {
        clearAllDialog: false,
        snackbar: {
          show: false,
          text: "Something went wrong",
          color: "red"
        },
      }
    },
    mounted: function () {
      this.load();
    },
    computed: {
      ...mapState(["items"])
    },
    methods: {
      ...mapMutations(["clearItems"]),
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
      },
      clearCompleted: function () {
        axios.delete("/todo/completed").then(() => {
          this.clearItems();
          this.load();
        }).catch(() => {
          this.showErrorSnackbar();
        });
      },
      clearAll: function () {
        axios.delete("/todo").then(() => {
          this.clearItems();
          this.showNoTodoSnackbar();
        }).catch(() => {
          this.showErrorSnackbar();
        }).finally(() => {
          this.clearAllDialog = false;
        })
      },
      openClearAllDialog: function () {
        if (!this.items.length) return;
        this.clearAllDialog = true;
      },
      showNoTodoSnackbar: function () {
        this.snackbar.text = "Congratulations!  You are all done now.";
        this.snackbar.color = "success";
        this.snackbar.show = true;
      },
      showErrorSnackbar: function () {
        this.snackbar.text = "Something went wrong.";
        this.snackbar.color = "red";
        this.snackbar.show = true;
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
