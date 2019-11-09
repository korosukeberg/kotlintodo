<template>
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
</template>

<script>
  import axios from "../plugins/axiosbase";
  import {mapMutations, mapState} from "vuex";

  export default {
    data: () => ({
      clearAllDialog: false
    }),
    computed: {
      ...mapState(["items"])
    },
    methods: {
      ...mapMutations(["clearItems", "showNoTodoSnackbar", "showErrorSnackbar"]),
      clearCompleted: function () {
        axios.delete("/todo/completed").then(() => {
          this.clearItems();
          this.$emit("loadToDos")
          //this.load();
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
    }
  };
</script>
