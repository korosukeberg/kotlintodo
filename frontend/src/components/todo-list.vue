<template>
  <v-list two-line flat class="pa-0">
    <v-card>
      <v-list-item-group multiple>
        <div v-for="(item, index) in this.items" :key="item.id">
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
</template>

<script>
  import axios from "../plugins/axiosbase";
  import {mapState} from "vuex";

  export default {
    data: () => ({}),
    computed: {
      ...mapState(["items"])
    },
    methods: {
      toggleCompleted: function (toggled) {
        axios.put("/todo" + "/" + toggled.id, {completed: toggled.completed})
      },
      clear: function (item, index) {
        axios.delete("/todo" + "/" + item.id).then(() => {
          this.items.splice(index--, 1)
        }).catch(() => {
          this.showErrorSnackbar();
        })
      },
    }
  };
</script>
