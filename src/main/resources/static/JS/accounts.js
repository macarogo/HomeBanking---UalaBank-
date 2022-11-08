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
      title: "Nueva Cuenta",
      text: "Seleccione el tipo de Cuenta a crear",
      icon: 'question',
      input: 'select',
      inputOptions: {
        AHORRO: 'Cuenta de Ahorro',
        CORRIENTE: 'Cuenta Corriente',
      },
      inputPlaceHolder: 'Seleccion..',
      showCancelButton: true,
      confirmButtonText: 'Crear',
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
          title: "Cuenta creada exitosamente!",
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
      title: "Estas seguro de querer eliminar esta cuenta?",
      text: "La accion no podra ser revertida",
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
              title: 'Exito!',
              text: 'La cuenta ha sido eliminada',
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

