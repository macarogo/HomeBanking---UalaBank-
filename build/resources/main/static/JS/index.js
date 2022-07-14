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
      },
      
      // register(){
        
      //   Swal.fire({
      //     title: "Are you sure?",
      //     text: "what do you want to register for?",
      //     icon: "warning",
      //     showCancelButton: true,
      //     confirmButtonColor: '#014377',
      //     cancelButtonColor: '#ff0000',
      //     confirmButtonText: 'Transfer now'
      //   })

      //   .then((result) => {
      //     if (result.isConfirmed ) {
            
      //         axios.post('/api/transactions',`accountDestination=${this.destinationAccount}&amount=${this.amout}&description=${this.description}&accountOrigen=${this.origenAccount}`,
      //         {headers:
      //           {'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log("hola"))

      //           .then (() => {
      //             Swal.fire({ 
      //               title:'Exitos!',
      //               text:"Your transaction was created successfully!",
      //               icon: "success",
      //               timer:2000
                    
      //           }).then(()=> location.reload())

      //         })

      //       .catch((error) => {
      //         Swal.fire({
      //         icon: 'error',
      //         text: error.response.data,
                
      //       })}) 


      //     };

      //   });
          
      // },
      
  },
  
    computed:{},
  
  }).mount('#app')


  

  