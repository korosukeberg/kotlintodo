<template>
  <v-app>
    <v-container id="container">
      <v-img :src="require('./assets/logo.svg')" class="my-3" contain height="200"></v-img>
      <v-text-field placeholder="What do you need to do?" solo @keydown.enter="add" v-model="newItemTitle" class="my-4">
        <template v-slot:append>
          <v-btn tile color="primary" @click="add">ADD</v-btn>
        </template>
      </v-text-field>

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

      <v-list two-line flat class="pa-0">
        <v-card>
          <v-list-item-group multiple>
            <div v-for="(item, index) in items" :key="item.id">
              <v-list-item>
                <v-list-item-action @click="toggleCompleted(item)">
                  <v-checkbox v-model="item.completed" color="primary"></v-checkbox>
                </v-list-item-action>
                <v-list-item-content>
                  <v-list-item-title
                    v-bind:class="{'primary--text done': item.completed}">{{ item.title }}
                  </v-list-item-title>
                </v-list-item-content>
                <v-btn text icon color="red lighten-3" style="display: none" class="clear-btn"
                       @click="clear(item, index)">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
              </v-list-item>
              <v-divider></v-divider>
            </div>
          </v-list-item-group>
        </v-card>
      </v-list>
    </v-container>

    <v-snackbar v-model="snackbar" :color="snackbarColor" :bottom="true" :right="true" :timeout=6000>
      {{ snackbarText }}
      <v-btn dark text @click="snackbar = false">Close</v-btn>
    </v-snackbar>
  </v-app>
</template>

<script>
  import axiosBase from "axios"

  const axios = axiosBase.create({
    baseURL: "http://localhost:8081"
  })

  export default {
    name: 'App',
    components: {},
    data: function () {
      return {
        newItemTitle: "",
        items: [],
        clearAllDialog: false,
        isProcessing: false,
        snackbar: false,
        snackbarText: "Something went wrong",
        snackbarColor: "red",
      }
    },
    mounted: function () {
      this.load();
    },
    methods: {
      load: function () {
        axios.get("/todo").then(response => {
          if (!response.data.length) {
            this.snackbarText = "Congratulations!  You are all done now.";
            this.snackbarColor = "success";
            this.snackbar = true;
            return;
          }
          for (let i = 0; i < response.data.length; i++) {
            this.items.push(response.data[i])
          }
        })
      },
      add: function () {
        if (!this.newItemTitle) return;
        if (this.isProcessing) return;

        this.isProcessing = true;
        axios.post("/todo", {title: this.newItemTitle}).then(response => {
          this.items.push(response.data);
          this.newItemTitle = "";
        }).catch(() => {
          this.snackbar = true;
        }).finally(() => {
          this.isProcessing = false;
        })
      },
      toggleCompleted: function (completedItem) {
        axios.put("/todo" + "/" + completedItem.id, {completed: completedItem.completed})
      },
      clear: function (item, index) {
        axios.delete("/todo", {data: [item.id]}).then(() => {
          this.items.splice(index--, 1)
        })
      },
      clearCompleted: function () {
        let completedItems = [];
        for (let i = 0; i < this.items.length; i++) {
          if (this.items[i].completed) {
            completedItems.push(this.items[i].id)
            this.items.splice(i--, 1);
          }
        }
        if (!completedItems.length) {
          return
        }
        axios.delete("/todo", {data: completedItems}).catch(() => {
          this.snackbar = true;
        });
      },
      clearAll: function () {
        axios.delete("/todo").then(() => {
          this.items = [];
        }).catch(() => {
          this.snackbar = true;
        }).finally(() => {
          this.clearAllDialog = false;
        })
      },
      openClearAllDialog: function () {
        if (!this.items.length) return;
        this.clearAllDialog = true;
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
