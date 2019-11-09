<template>
  <v-text-field placeholder="What do you need to do?" solo @keydown.enter="add" v-model="newItemTitle" class="my-4">
    <template v-slot:append>
      <v-btn tile color="primary" @click="add">ADD</v-btn>
    </template>
  </v-text-field>
</template>

<script>
  import axios from "../plugins/axiosbase";
  import {mapMutations, mapState} from "vuex";

  export default {
    data: () => ({
      newItemTitle: ""
    }),
    computed: {
      ...mapState(["isProcessing", "items"])
    },
    methods: {
      ...mapMutations(["beginProcessing", "endProcessing"]),
      add: function () {
        if (!this.newItemTitle) return;
        if (this.isProcessing) return;

        this.beginProcessing();
        axios.post("/todo", {title: this.newItemTitle}).then(response => {
          this.items.push(response.data);
          this.newItemTitle = "";
        }).catch(() => {
          this.showErrorSnackbar();
        }).finally(() => {
          this.endProcessing();
        })
      },
    }
  };
</script>
