window.loginApp = new Vue({
  el: '#app',
  data: {
    username: null,
    password: null
  },
  methods:{
    checkForm: function (e) {
      if (this.username && this.password) {
        return true;
      }
      e.preventDefault();
    }
  }
})