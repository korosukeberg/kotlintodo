import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const state = {
  isProcessing: false,
  items: [],
}

const mutations = {
  beginProcessing() {
    this.state.isProcessing = true;
  },
  endProcessing() {
    this.state.isProcessing = false;
  },
  clearItems() {
    this.state.items = [];
  }
}

export default new Vuex.Store({
  state,
  mutations
});
