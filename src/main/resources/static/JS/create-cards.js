Vue.createApp({//un objeto que tiene propiedades
    data() {//data un objeto que retorna mas propiedades 
      return {
       client:{},
       cards: {},
       cardType:false,
       cardColor:"",
       cardCredit:"",
       cardDebit:"",
       limiteCardDebit:"",
     }
    },
  
      created(){
          axios.get("http://localhost:8080/api/clients/current")// axios es una libreria basada en promesas
          .then(datos =>{ //El método then devuelve una promesa que permite encadenar métodos
              this.client = datos.data//metodo get obtiene
              this.cards = datos.data.cards
              this.cardDebit = this.cards.filter(card => card.type == "DEBITO")
              this.cardCredit = this.cards.filter(card => card.type == "CREDITO")

          })
      },
  
    methods : {

      createCards(){
        console.log(this.cardType)
        if(this.cardType == false){
          axios.post('/api/clients/current/cards', `cardType=DEBITO&cardColor=${this.cardColor}`,
          {headers:{'content-type': 'application/x-www-form-urlencoded'}})
          .then(response => window.location.href="http://localhost:8080/web/cards.html")
          .catch(error=> {
            this.limiteCardDebit="I exceed the number of Debit card limits"
          })
        }
        else {
          axios.post('/api/clients/current/cards', `cardType=CREDITO&cardColor=${this.cardColor}`,
          {headers:{'content-type': 'application/x-www-form-urlencoded'}})
          .then(response => window.location.href="http://localhost:8080/web/cards.html")
          .catch(error=>{
            this.limiteCardCredit="I exceed the number of Credit card limits"
          })
        }

      },

  },
  
    computed:{},
  
  }).mount('#app')