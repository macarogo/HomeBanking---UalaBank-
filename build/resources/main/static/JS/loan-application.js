Vue.createApp({s
    data() { 
      return {
  
        client: {},
        loans:{},
        accountsClient:{},

        idLoan: 2,
        amount: "",
        payments: "",
        accountDestination:"",
        loanSelec:{},
  
     }
    },
  
      created(){
          axios.get("/api/clients/current")s
          .then(datos =>{ 
              this.client = datos.data
              this.accountsClient = datos.data.accounts.sort((a,b) => a.id - b.id)
              console.log(this.client.firstName)
            
          })

          axios.get("/api/loans")
          .then(datos =>{
              this.loans = datos.data 
              console.log(this.loans)
                    
          }).then(() =>{
            this.cambiarValorSwit()
          })
          
      },
  
    methods : {

      cambiarValorSwit(){
        this.loanSelec = this.loans.filter(loan => loan.id == this.idLoan)[0]
      
      },
      
      createLoan(){

        Swal.fire({
          title: "Are you sure?",
          text: "who wants to make a loan?",
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: '#014377',
          cancelButtonColor: '#ff0000',
          confirmButtonText: 'Transfer now'
        })

        .then((result) => {
          if (result.isConfirmed ) {
            let aux = {id:parseInt(this.idLoan), amount:this.amount, payments:parseInt(this.payments), accountDestination:this.accountDestination}
            console.log(aux)
              axios.post('/api/loans', aux)

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

      selecPayments(){
        let aux = [...this.loans]
        aux = aux.filter((loan) => this.idLoan == loan.id);
        return aux[0].payments

      },
    
  },
  
  computed:{},
  
  }).mount('#app')