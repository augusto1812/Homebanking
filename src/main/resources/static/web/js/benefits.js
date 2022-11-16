var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        discounts: {},
        cashbacks: {},
        errorToats: null,
        errorMsg: null,
    },
    created: function() {
        this.getData;
        this.getDiscounts;
        this.getCashbacks;
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current")
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;
            })
            .catch((error)=>{
                // handle error
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
         getDiscounts: function(){
                    axios.get("/api/discounts")
                    .then((response) => {
                        this.discounts = response.data;
                        console.log(this.discounts);
                        this.discounts.forEach(discount => {
                           let keys = Object.keys(discount.percentagePerCard);
                           discount.percentagePerCard = keys.sort().reduce((acumulador,key) => {
                                acumulador[key] = discount.percentagePerCard[key];
                                return acumulador;
                           },{});
//                            discount.sort((a,b) => a.percentagePerCard.)
                        } )
                    })
                    .catch((error)=>{
                        // handle error
                        this.errorMsg = "Error getting data";
                        this.errorToats.show();
                    })
                },
           getCashbacks: function(){
                             axios.get("/api/cashbacks")
                             .then((response) => {
                                 this.cashbacks = response.data;
                             })
                             .catch((error)=>{
                                 // handle error
                                 this.errorMsg = "Error getting data";
                                 this.errorToats.show();
                             })
                         },
           formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"   
                this.errorToats.show();
            })
        },
//        create: function(){
//            axios.post('http://localhost:8080/api/clients/current/accounts')
//            .then(response => window.location.reload())
//            .catch((error) =>{
//                this.errorMsg = error.response.data;
//                this.errorToats.show();
//            })
//        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
        this.getDiscounts();
        this.getCashbacks();
    }
})
