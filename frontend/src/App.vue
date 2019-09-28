<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png">
    <div id="header">
      <input type="text" id="todoinput" class="form-control" v-on:keyup.enter="add" v-model="newItemTitle" />
      <input type="button" class="btn btn-primary" value="ADD" v-on:click="add" />
      <input type="button" class="btn btn-warning" value="CLEAR COMPLETED" v-on:click="clearCompleted" />
      <input type="button" class="btn btn-danger" value="CLEAR ALL" v-on:click="clearAll" />
    </div>

    <ul id="todolist" class="list-group">
      <li class="list-group-item" v-for="item in items">
        <label v-bind:class="{done: item.isCompleted}"> <input type="checkbox" v-model="item.isCompleted" />{{ item.title }} </label>
      </li>
    </ul>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import axiosBase from "axios"
const axios = axiosBase.create({
  baseURL: "http://localhost:8081"
})

export default {
  name: 'app',
  components: {
    HelloWorld
  },
  data: function () {
    return {
      newItemTitle: "",
      items: []
    }
  },
  mounted: function () {
    this.load();
  },
  methods: {
    //load todos when opened <- DONE!!!
    //save todos when added
    //delete todos when deleted
    load: function () {
      axios.get("/todo").then((response) => {
        for (let i = 0; i < response.data.length; i++) {
          this.items.push(response.data[i])
        }
      })
    },
    add: function() {
      if (!this.newItemTitle) {
        return;
      }
      let newItem = {
        id: this.items.length ? this.items.reduce((a,b)=>a.id>b.id?a:b).id+1 : 1,
        title: this.newItemTitle,
        isCompleted: false
      }
      axios.post("/todo", newItem).then(response => {
        this.items.push(newItem);
        this.newItemTitle = "";
      }, error => {
        console.log(error);
      });
    },
    clearCompleted: function() {
      let completedItems = [];
      for (let i = 0; i < this.items.length; i++) {
        if (this.items[i].isCompleted) {
          completedItems.push(this.items[i].id)
          this.items.splice(i--, 1);
        }
      }
      if (!completedItems.length) {
        return
      }
      axios.delete("/todo", {data: completedItems}).then(response => {
              }
      , error => {
        console.log(error);
      });
    },
    clearAll: function() {
      let itemIds = this.items.map(value => {
        return value.id
      })
      axios.delete("/todo", {data: itemIds}).then(response => {
        this.items = [];
        }, error => {
        console.log(error);
      });
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: 600px;
  margin: 60px auto;
}
h2 {
  display: flex;
  justify-content: center;
}
#header {
  display: flex;
  justify-content: center;
}
#todoinput {
  width: 100%;
}
#todolist {
  margin: 60px auto;
}
.done {
  text-decoration: line-through;
}

</style>
