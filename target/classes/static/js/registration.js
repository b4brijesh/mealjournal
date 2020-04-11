window.signUpApp = new Vue({
  el: '#app',
  data: {
    fullname: null,
    username: null,
    password: null
  },
  methods:{
    checkForm: function (e) {
      if (this.fullname && this.username && this.password) {
        return true;
      }
      e.preventDefault();
    }
  }
})