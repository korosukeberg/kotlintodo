import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

const state = {
  isProcessing: false,
  items: [],
}

const mutations = {
  beginProcessing(isProcessing) {
    isProcessing = true;
  },
  endProcessing(isProcessing) {
    isProcessing = false;
  }
}

export default new Vuex.Store({
  state,
  mutations
});
