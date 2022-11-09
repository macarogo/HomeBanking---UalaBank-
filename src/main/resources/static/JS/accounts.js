Vue.createApp({
  data() {
    return {
      client: {},
      accountsClient: {},
      loans:{},
      tipoDeCuenta:"",
    }
  },

    created(){
      axios.get("/api/clients/current")                                                                    
      .then(datos =>{ 
        this.client = datos.data
        this.accountsClient = datos.data.accounts.sort((a,b) => a.id - b.id)
        this.loans = datos.data.loans
      })
    },

  methods : {
  logout(){
    axios.post('/api/logout').then(response => window.location.href="/web/index.html")
  },

  createAccount(){
    Swal.fire({
      title: "New account",
      text: "Select the type of Account to create",
      icon: 'question',
      input: 'select',
      inputOptions: {
        AHORRO: 'Savings account',
        CORRIENTE: 'Current account',
      },
      inputPlaceHolder: 'Selection..',
      showCancelButton: true,
      confirmButtonText: 'To create',
    inputValidator: (value) => {
      return new Promise((resolve) => {
        if (value === 'AHORRO') {
          this.tipoDeCuenta = "AHORRO"
          resolve()
        }
        if (value === 'CORRIENTE') {
          this.tipoDeCuenta = "CORRIENTE"
          resolve()
        }
        if (value === '') {
          this.tipoDeCuenta = ""
          resolve()
        }
      })
    }})
    .then(result => {
      if (result.isConfirmed) {
        Swal.fire({
          title: "Account created successfully!",
          icon: 'success'
        })
        .then(result => {
          axios.post(`/api/clients/current/accounts`, `type=${this.tipoDeCuenta}`)
          .then(response => {
            location.reload()
          })
        })
        .catch(error => {
          Swal.fire({
            icon: 'error',
            text: error.response.data,
          })
        })
      }
    })
  },

  removeAccount(id) {
    Swal.fire({
      title: "Are you sure you want to delete this account?",
      text: "The action cannot be reversed",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#014377',
      cancelButtonColor: '#ff0000',
      confirmButtonText: 'Confirmar!'
    })
    .then((result) => {
      if (result.isConfirmed) {
        axios.patch(`/api/clients/current/accounts/${id}`)
          .then(() => {
            Swal.fire({
              icon: 'success',
              title: 'Successes!',
              text: 'The account has been deleted',
              timer: 2000
            })
            .then(() => location.reload())
          })
        .catch((error) => {
          Swal.fire({
            icon: 'error',
            text: error.response.data,
          })
        })
      };
    });
  },
},

computed:{},

}).mount('#app')