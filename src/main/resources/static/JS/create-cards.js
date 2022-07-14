Vue.createApp({
    data() {
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
          axios.get("/api/clients/current")
          .then(datos =>{
              this.client = datos.data
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
          .then(response => window.location.href="/web/cards.html")
          .catch(error=> {
            this.limiteCardDebit="I exceed the number of Debit card limits"
          })
        }
        else {
          axios.post('/api/clients/current/cards', `cardType=CREDITO&cardColor=${this.cardColor}`,
          {headers:{'content-type': 'application/x-www-form-urlencoded'}})
          .then(response => window.location.href="/web/cards.html")
          .catch(error=>{
            this.limiteCardCredit="I exceed the number of Credit card limits"
          })
        }

      },

  },
  
    computed:{},
  
  }).mount('#app')