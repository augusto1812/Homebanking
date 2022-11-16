var app = new Vue({
    el:"#app",
    data:{
        errorToats: null,
        errorMsg: null,
        typeAccount:"",
        typesAccount:[],
        amountSale:0,
        amountBuy:0,
        typesCurrencies:[],
        typeCurrencySale:"",
        typeCurrencyBuy:"",
        listAccounts:[],
        sourceAccountNumber:"",
        destinationAccountNumber:"",
        sourceAccount:{},
        destinationAccount:{},
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
        getTypesAccount: function(){
            axios.get("/api/account/getTypesAccount")
            .then((response) => {
                this.typesAccount = response.data;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
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
        getAccounts: function(){
            axios.get("/api/clients/current/accounts")
            .then((response) => {
                this.listAccounts = response.data;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        buySaleCurrency: function(){
            axios.post(`/api/currency/buySaleCurr?amount=${this.amountSale}&currBuy=${this.typeCurrencyBuy}&currSale=${this.typeCurrencySale}&fromAccountNumber=${this.sourceAccountNumber}&toAccountNumber=${this.destinationAccountNumber}`)
            .then(response => window.location.href = "/web/accounts.html")
            .catch((error) =>{
                this.errorMsg = error.response.data;
                this.errorToats.show();
            })
        },
        changedSourceAcc: function(){
            const result = this.listAccounts.filter(acc => acc.number == this.sourceAccountNumber);
            this.sourceAccount = result[0];
            this.typeCurrencySale = this.sourceAccount.currencyType;
        },
        changedDestinationAcc: function(){
            const result = this.listAccounts.filter(acc => acc.number == this.destinationAccountNumber);
            this.destinationAccount = result[0];
            this.typeCurrencyBuy = this.destinationAccount.currencyType;
        },
        getBuyAmount: function(){
            axios.get(`/api/currency/getBuyAmount?amount=${this.amountSale}&currBuy=${this.typeCurrencyBuy}&currSale=${this.typeCurrencySale}`)
            .then((response) => {
                this.amountBuy = response.data;
            })
            .catch((error) => {
                this.errorMsg = error.response.data;
                this.errorToats.show();
            })
        },
        getSaleAmount: function(){
            axios.get(`/api/currency/getSaleAmount?amount=${this.amountBuy}&currBuy=${this.typeCurrencyBuy}&currSale=${this.typeCurrencySale}`)
            .then((response) => {
                this.amountSale = response.data;
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
        //this.getTypesAccount();
        this.getTypesCurrencies();
        this.getAccounts();
    }
})