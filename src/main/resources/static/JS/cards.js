Vue.createApp({
    data() {
      return {
        client:{},
        cards: [],
        cardCredit:{},
        cardDebit:{},
      }
    },
      
    created(){
      this.cargardatosiniciales();
    },
  
  methods : {
    cardsDate(fecha){
      fecha = fecha.split("-")
      fecha[0] = fecha[0].substring(2,4) 
      return fecha[1] + "/" + fecha[0]
    },

    vencimiento(){
      let date= new Date();
      actual = date.toISOString().split('T')[0]
      this.cards.forEach(card => {
        if(card.truDate.valueOf() < actual.valueOf()){
          console.log(card.truDate + "Card Vencida")
          axios.patch(`/api/cards/expired/${card.id}`)
        }
        else{
          console.log(card.truDate + "Card Active")
        }
      });
    },

    cargardatosiniciales(){
      axios.get("/api/clients/current")
      .then(datos => {
        this.client = datos.data
        this.cards = datos.data.cards
        this.cardDebit = this.cards.filter(card => card.type == "DEBITO")
        this.cardCredit = this.cards.filter(card => card.type == "CREDITO")
      }).then(()=> {
        this.vencimiento()
      })
      .catch(function (error) {    
        console.log(error);
      })
    },

    cargardatos(){
      axios.get("/api/clients/current")
      .then(datos => {
        this.client = datos.data
        this.cards = datos.data.cards
        this.cardDebit = this.cards.filter(card => card.type == "DEBITO")
        this.cardCredit = this.cards.filter(card => card.type == "CREDITO")
      })
      .catch(function (error) {
        console.log(error);
      })
    },

    removeCard(id) {
      Swal.fire({
        title: "Are you sure you want to delete this card?",
        text: "The action cannot be reversed",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#014377',
        cancelButtonColor: '#ff0000',
        confirmButtonText: 'Confirmar!'
      })
      .then((result) => {
        if (result.isConfirmed) {   
          axios.patch(`/api/clients/current/cards/${id}`)
          .then(() => {
            Swal.fire({
              icon: 'success',
              title: 'Successes!',
              text: 'The Card has been deleted',
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

    logout(){
      axios.post('/api/logout').then(response => window.location.href="/web/index.html")
    },
  },
  
  computed:{},
  
}).mount('#app')
