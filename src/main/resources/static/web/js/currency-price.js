var app = new Vue({
    el:"#app",
    data:{
        clientAccounts: [],
        clientAccountsTo: [],
        debitCards: [],
        errorToats: null,
        errorMsg: null,
        accountFromNumber: "VIN",
        accountToNumber: "VIN",
        trasnferType: "own",
        amount: 0,
        description: "",
        typesCurrencies:[],
        firstCurrency:"",
        secondCurrency:"",
        priceData:{}
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current/accounts")
            .then((response) => {
                //get client ifo
                this.clientAccounts = response.data;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        checkTransfer: function(){
            if(this.accountFromNumber == "VIN"){
                this.errorMsg = "You must select an origin account";
                this.errorToats.show();
            }
            else if(this.accountToNumber == "VIN"){
                this.errorMsg = "You must select a destination account";
                this.errorToats.show();
            }else if(this.amount == 0){
                this.errorMsg = "You must indicate an amount";
                this.errorToats.show();
            }
            else if(this.description.length <= 0){
                this.errorMsg = "You must indicate a description";
                this.errorToats.show();
            }else{
                this.modal.show();
            }
        },
        transfer: function(){
            let config = {
                /*headers: {
                    'content-type': 'application/x-www-form-urlencoded'
                }*/
            }
            axios.post(`/api/transactions?fromAccountNumber=${this.accountFromNumber}&toAccountNumber=${this.accountToNumber}&amount=${this.amount}&description=${this.description}`,config)
            .then(response => {
                this.modal.hide();
                this.okmodal.show();
            })
            .catch((error) =>{
                this.errorMsg = error.response.data;
                this.errorToats.show();
            })
        },
        changedType: function(){
            this.accountFromNumber = "VIN";
            this.accountToNumber = "VIN";
        },
        changedFrom: function(){
            if(this.trasnferType == "own"){
                this.clientAccountsTo = this.clientAccounts.filter(account => account.number != this.accountFromNumber);
                this.accountToNumber = "VIN";
            }
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
            axios.get("/api/account/getTypesCurrencies")
            .then((response) => {
                //get client ifo
                this.typesCurrencies = response.data;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        getPrice: function(){
            const urlParams = new URLSearchParams(window.location.search);
            const currBuy = urlParams.get('currBuy');
            const currSale = urlParams.get('currSale');
            axios.get("/api/currency/getPrice/"+currBuy+"/"+currSale)
            .then((response) => {
                //get client ifo
                this.priceData = response.data;
            })
            .catch((error) => {
                this.errorMsg = "Error getting data";
                this.errorToats.show();
            })
        },
        getPricePage: function(){
//            url = "/currency-prices.html?currBuy="+this.firstCurrency+"&currSale="+this.secondCurrency;
//            window.location.href="http://localhost:8080/web/currency-prices.html?currBuy=usd&currSale=ars";
//            url2=`currency-prices.html?currBuy=${this.firstCurrency}&currSale=${this.secondCurrency}`;
//            url3='currency-prices.html?currBuy=usd&currSale=ars';
//            window.location.replace('currency-prices.html?currBuy=usd&currSale=ars');
               const currBuyAux = this.firstCurrency;
               const currSaleAux= 'ars';



               //window.location.replace('currency-prices.html?currBuy='+this.firstCurrency+'&currSale=ars');


                //funka
               window.location.replace('currency-prices.html?currBuy=usd&currSale=ars');
        },
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.getData();
        this.getTypesCurrencies();
        this.getPrice();
    }
})