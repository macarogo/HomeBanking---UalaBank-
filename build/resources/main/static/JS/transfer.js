Vue.createApp({//un objeto que tiene propiedades
    data() {//data un objeto que retorna mas propiedades 
      return {
  
        account: [],

        origenAccount:"",
        amout:"",
        description:"",
        destinationAccount:"",

     }
    },
  
      created(){

   
          axios.get("/api/clients/current" )// axios es una libreria basada en promesas
          .then(datos =>{ //El método then devuelve una promesa que permite encadenar métodos
              this.account = datos.data.accounts//metodo get obtiene
              
              console.log(this.account)
             
            
          })
      },
  
    methods : {

      newtransaction(){
        
        Swal.fire({
          title: "Are you sure?",
          text: "who wants to make a transfer?",
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: '#014377',
          cancelButtonColor: '#ff0000',
          confirmButtonText: 'Transfer now'
        })

        .then((result) => {
          if (result.isConfirmed ) {
            
              axios.post('/api/transactions',`accountDestination=${this.destinationAccount}&amount=${this.amout}&description=${this.description}&accountOrigen=${this.origenAccount}`,
              {headers:
                {'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log("hola"))

                .then (() => {
                  Swal.fire({ 
                    title:'Exitos!',
                    text:"Your transaction was created successfully!",
                    icon: "success",
                    timer:2000
                    
                }).then(()=> location.reload())

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