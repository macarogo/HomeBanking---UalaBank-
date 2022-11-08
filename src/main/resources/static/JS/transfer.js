Vue.createApp({
    data() {
      return {
        account: [],
        origenAccount:"",
        amout:"",
        description:"",
        destinationAccount:"",
      }
    },

      created(){
        axios.get("/api/clients/current" )
          .then(datos =>{ 
            this.account = datos.data.accounts     
            console.log(this.account)               
        })
      },
  
    methods : {
      newtransaction(){   
        Swal.fire({
          title: "Are you sure?",
          text: "To make the transfer?",
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: '#014377',
          cancelButtonColor: '#ff0000',
          confirmButtonText: 'Transfer now'
        })
        .then((result) => {
          if (result.isConfirmed ) {        
            axios.post('/api/transactions',`accountDestination=${this.destinationAccount}&amount=${this.amout}&description=${this.description}&accountOrigen=${this.origenAccount}`,)
              .then(response => console.log("hola"))
                .then (() => {
                  Swal.fire({ 
                    title:'Exitos!',
                    text:"Your transaction was created successfully!",
                    icon: "success",
                    timer:2000                   
                })
                .then(()=> location.reload())
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