Vue.createApp({
  data() {
    return {
      email:"",
      password:"",
      firstName:"",
      lastName:"",
      emailRegister:"",
      passwordRegister:"",
      errorInicio:"",
      errorRegister:"",
    }    
  },

    created(){},

  methods : {
    iniciarSeccion(){
      axios.post('/api/login',`email=${this.email}&password=${this.password}`,)
        .then((result) => {
          this.email == "admin@mindhub.com" && this.password == "adminmainhub3" ? window.location.href="/manager.html" : window.location.href="/web/accounts.html"
        })
        .catch(error =>{ 
          this.errorInicio = "The email or password already exist"
        });   
    },
    
    register(){       
      Swal.fire({
        title: "Are you sure?",
        text: "to register?",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: '#014377',
        cancelButtonColor: '#ff0000',
        confirmButtonText: 'register'
      })
      .then((result) => {
        if (result.isConfirmed ) {           
          axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`,)
            .then(response => console.log("hola"))
              .then (() => {
                Swal.fire({ 
                  title:'Exitos!',
                  text:"you registered!",
                  icon: "success",
                  timer:2000                   
              }).then(response => {
                this.email=this.emailRegister,
                this.password=this.passwordRegister,
                this.iniciarSeccion()
              })
            })
          .catch((error) => {
            Swal.fire({
            icon: 'error',
            text: error.response.data,                
          })}) 
        };
      });          
    },     
},

  computed:{},  
}).mount('#app') 