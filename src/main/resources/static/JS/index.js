Vue.createApp({//un objeto que tiene propiedades
    data() {//data un objeto que retorna mas propiedades 
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
  
      created(){

      },
  
    methods : {
      iniciarSeccion(){
        axios.post('/api/login',`email=${this.email}&password=${this.password}`,
        {headers:
          {'content-type':'application/x-www-form-urlencoded'}})
          .then((result) => {
            this.email == "admin@mindhub.com" && this.password == "adminmainhub3" ? window.location.href="/manager.html" : window.location.href="/web/accounts.html"
          })
          .catch(error =>{ 
            this.errorInicio = "The email or password already exist"//error no esta autorizado//envio a que se registre
          });
          
      },

      register(){
        axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`,
        {headers:{'content-type': 'application/x-www-form-urlencoded'}})
        .then(response => {
          this.email=this.emailRegister,
          this.password=this.passwordRegister,
          this.iniciarSeccion()
        })
        .catch(error =>{
          if(error.response.data == "Name already in use"){
            this.errorRegister="Registered email"
          } 
        });
      }
      
  },
  
    computed:{},
  
  }).mount('#app')

  

  