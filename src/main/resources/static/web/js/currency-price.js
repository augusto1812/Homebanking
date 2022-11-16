var app = new Vue({
    el:"#app",
    data:{
        errorToats: null,
        errorMsg: null,
        typesCurrencies:[],
        firstCurrency:"",
        secondCurrency:"",
        priceData:{},
        firstCurrencyPrice:"",
        secondCurrencyPrice:""
    },
    methods:{
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        finish: function(){
            window.location.reload();
        },
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"
                this.errorToats.show();
            })
        },
        getTypesCurrencies: function(){
            axios.get("/api/currency/getTypesCurrencies")
            .then((response) => {
                this.typesCurrencies = response.data;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        getPricePage: function(){
               axios.get("/api/currency/getPrice/"+this.firstCurrency+"/"+this.secondCurrency)
               .then((response) => {
                   this.firstCurrencyPrice = response.data.compra;
                   this.secondCurrencyPrice = response.data.venta;
               })
               .catch((error) => {
                   this.errorMsg = error.response.data;
                   this.errorToats.show();
               })
        },
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.getTypesCurrencies();
    }
})