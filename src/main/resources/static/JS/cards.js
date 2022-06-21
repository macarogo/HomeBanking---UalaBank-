Vue.createApp({//un objeto que tiene propiedades
    data() {//data un objeto que retorna mas propiedades 
      return {
       client:{},
        cards: [],//declaramos las variables
        cardCredit:{},
        cardDebit:{},
  
     }
    },
  
      created(){
        this.cargardatosiniciales();
      },
  
    methods : {
      // 2027-05-20  05/27
      // fecha [0] 2022
      // 
      
      cardsDate(fecha){
        fecha = fecha.split("-")
        fecha[0] = fecha[0].substring(2,4) 
        return fecha[1] + "/" + fecha[0]
      },

      
      vencimiento(){
        let date= new Date();
        actual = date.toISOString().split('T')[0]
        console.log(actual)

        this.cards.forEach(card => {
          if(card.truDate.valueOf() < actual.valueOf()){
            console.log(card.truDate + "Card Vencida")
            axios.patch(`http://localhost:8080/api/cards/expired/${card.id}`)
          }
          else{
            console.log(card.truDate + "Card Active")
          }
        });
      },

      cargardatosiniciales(){
        axios.get("http://localhost:8080/api/clients/current")
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
        axios.get("http://localhost:8080/api/clients/current")
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
            title: "Estas seguro de querer eliminar esta tarjeta?",
            text: "La accion no podra ser revertida",
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
              title: 'Exito!',
              text: 'La Tarjeta ha sido eliminada',
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
        axios.post('/api/logout').then(response => window.location.href="http://localhost:8080/web/index.html")
      },

    },
  
    computed:{},
  
}).mount('#app')
