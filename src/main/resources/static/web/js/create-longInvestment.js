var app = new Vue({
    el:"#app",
    data:{
        errorToats: null,
        errorMsg: null,
        amount:0,
        period:"",
        account: "VIN",
        clientAccounts:[]
    },
    methods:{
       getData: function(){
           axios.get("/api/clients/current/accounts")
           .then((response) => {
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
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Sign out failed"
                this.errorToats.show();
            })
        },
        create: function(event){
            event.preventDefault();
            if(this.period == ""){
                this.errorMsg = "You must select a period investment";
                this.errorToats.show();
            }else if( this.amount<= 0 || this.amount <= 1000){
             this.errorMsg = "Amount must be greater than $1.000";
             this.errorToats.show();
            }
            else{
               axios.post("/api/investments",{accountId: getAccountId(account), amount: this.amount, periodType: this.period })
                //.then(response => window.location.href = "/web/cards.html")
                .catch((error) =>{
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
            }
        },
        getAccountId: function( number){
        clientAccounts.forEach( (account) => {
            if (account.number == number){
                return account.id
                }
            });
        },
    },

    mounted: function(){
       this.getData();
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
})