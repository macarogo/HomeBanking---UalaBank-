Vue.createApp({//un objeto que tiene propiedades
    data() {//data un objeto que retorna mas propiedades 
      return {

        clientes: [],//declaramos las variables
        cliente: {},
        firstName:"",
        lastName: "",
        email:"",
        password:"",
        
        loanName: "",
        loanMaxAmount: 0,
        loanPayments: [],
        loanInterest: 0,

     }
    },

      created(){
          axios.get("http://localhost:8080/api/clients")// axios es una libreria basada en promesas
          .then(datos =>{ //El método then devuelve una promesa que permite encadenar métodos
              this.clientes = datos.data//metodo get obtiene
              console.log(this.clientes)
          })
      },

    methods : {
        dibujarTabla (){
            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,
            {headers:
              {'content-type':'application/x-www-form-urlencoded'}})
                .then(response => {
                    location.reload()
                })
        },

        editarCliente(id){  
            axios.patch(`http://localhost:8080/api/clients/modify/${id}`,`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}`,
            {headers:{ 'content-type':'application/json'}})
            .then(response => {
                location.reload()    
            .catch(errror => console.log(errror))           
            })
        },

        borrarCliente(id){           
            axios.delete(`http://localhost:8080/api/clients/deleteClient/${id}`,`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}`,
            {headers:{ 'content-type':'application/json'}})
            .then(function (){
                location.reload()
            .catch(errror => console.log(errror))
            })          
        },


    addLoan() {
        this.loanPayments = this.loanPayments.split(',')

        axios.post('/api/newLoan', { "name": this.loanName, "maxAmount": this.loanMaxAmount, "payments": this.loanPayments, "interest": this.loanInterest },
            {headers:{ 'content-type': 'application/json' }})
            .then(() => location.reload()
        )
    },


    logout(){
        axios.post('/api/logout').then(response => window.location.href="http://localhost:8080/web/index.html")
        
    },
    
},

    computed:{},

}).mount('#app')