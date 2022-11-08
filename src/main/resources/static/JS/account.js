Vue.createApp({
    data() {
      return { 
        account: [],
        transactions: [],
        desde:"",
        hasta:"",
        client: [],
        accountsClient: {},
      }
    },
  
      created(){
        const urlParams = new URLSearchParams(window.location.search);
        const myParam = urlParams.get('id');
        console.log(myParam)
          axios.get("/api/accounts/" + myParam)
          .then(datos =>{
            this.account = datos.data
            this.transactions = datos.data.transactions
            this.transactions = this.transactions.sort((a,b) => a.id - b.id)             
          })
          axios.get("/api/clients/current")
          .then(datos =>{ 
            this.client = datos.data              
          })
      },
  
    methods : {
      logout(){
        axios.post('/api/logout').then(response => window.location.href="/web/index.html")       
      },

      download(){
        const urlParams = new URLSearchParams(window.location.search);
        const myParam = urlParams.get('id');
        axios.post(`/api/pdf/${myParam}`,`desde=${this.desde}&hasta=${this.hasta}`)
          .then(console.log("download"))
          .catch(error => {
        })
      }
  },
  
    computed:{},
  
  }).mount('#app')
