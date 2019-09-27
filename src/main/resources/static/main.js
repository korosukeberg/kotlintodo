const vm = new Vue({
  el: "#app",
  data: {
    newItemTitle: "",
    items: [{ id: 1, title: "項目1", isCompleted: false }]
  },
  methods: {
    add: function() {
      if (!this.newItemTitle) {
        return;
      }
      this.items.push({
        id: this.items.length ? this.items.reduce((a,b)=>a.id>b.id?a:b).id+1 : 1,
        title: this.newItemTitle,
        isCompleted: false
      });
      this.newItemTitle = "";
    },
    clearCompleted: function() {
      for (i = 0; i < this.items.length; i++) {
        if (this.items[i].isCompleted) {
          this.items.splice(i--, 1);
        }
      }
    },
    clearAll: function() {
      this.items = [];
    }
  }
});
