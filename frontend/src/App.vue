<template>
  <v-app>
    <v-container id="container">
      <v-img :src="require('./assets/logo.svg')" class="my-3" contain height="200"></v-img>
      <v-text-field placeholder="What do you need to do?" solo @keyup.enter="add" v-model="newItemTitle" class="my-4">
        <template v-slot:append>
          <v-btn tile color="primary" @click="add">ADD</v-btn>
        </template>
      </v-text-field>

      <custominput>
        <template v-slot:buttonlabel>I'm called from parent vue</template>
      </custominput>

      <v-layout justify-end v-show="this.items.length">
        <v-btn text small color="warning" @click="clearCompleted">CLEAR COMPLETED</v-btn>
        <v-btn text small color="error" @click="openClearAllDialog">CLEAR ALL</v-btn>
        <v-dialog v-model="dialog" max-width="290">
          <v-card>
            <v-card-title>Are you sure you want to clear all your todo?</v-card-title>
            <v-card-actions>
              <div class="flex-grow-1"></div>
              <v-btn color="blue darken-1" text @click="dialog = false">CANCEL</v-btn>
              <v-btn color="blue darken-1" text @click="clearAll">OK</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-layout>

      <v-list two-line flat class="pa-0">
        <v-card>
          <v-list-item-group multiple>
            <template v-for="(item, index) in items">
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
            </template>
          </v-list-item-group>
        </v-card>
      </v-list>
      <!-- <HelloWorld/> -->
    </v-container>
  </v-app>
</template>

<script>
  import HelloWorld from './components/HelloWorld';
  import axiosBase from "axios"
  import custominput from './components/custominput';

  const axios = axiosBase.create({
    baseURL: "http://localhost:8081"
  })

  export default {
    name: 'App',
    components: {
      HelloWorld,
      custominput,
    },
    data: function () {
      return {
        newItemTitle: "",
        items: [],
        dialog: false,
      }
    },
    mounted: function () {
      this.load();
    },
    methods: {
      load: function () {
        axios.get("/todo").then(response => {
          response.data.sort(function (a, b) {
            if (a.id < b.id) return -1;
            if (a.id > b.id) return 1;
            return 0;
          });
          for (let i = 0; i < response.data.length; i++) {
            this.items.push(response.data[i])
          }
        })
      },
      add: function () {
        if (!this.newItemTitle) {
          return;
        }
        let newItem = {
          id: this.items.length ? this.items.reduce((a, b) => a.id > b.id ? a : b).id + 1 : 1,
          title: this.newItemTitle,
          completed: false
        }
        axios.post("/todo", newItem).then(response => {
          this.items.push(newItem);
          this.newItemTitle = "";
        }, error => {
          console.log(error);
        });
      },
      toggleCompleted: function (completedItem) {
        axios.post("/todo" + "/" + completedItem.id, {completed: completedItem.completed})
      },
      clear: function (item, index) {
        axios.delete("/todo", {data: [item.id]}).then(response => {
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
        axios.delete("/todo", {data: completedItems}).then(response => {
        }, error => {
          console.log(error);
        });
      },
      clearAll: function () {
        let itemIds = this.items.map(value => {
          return value.id
        })
        axios.delete("/todo", {data: itemIds}).then(response => {
          this.items = [];
          this.dialog = false;
        }, error => {
          console.log(error);
        });
      },
      openClearAllDialog: function () {
        if (!this.items.length) return;
        this.dialog = true;
      }
    }
  }
</script>
<style>
  .done {
    text-decoration: line-through;
  }

  #container {
    max-width: 550px;
  }

  .v-list-item:hover .clear-btn {
    display: block !important;
  }

</style>
