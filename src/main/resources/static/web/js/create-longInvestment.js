var app = new Vue({
    el:"#app",
    data:{
        errorToats: null,
        errorMsg: null,
        amount:0,
        period:"",
        accountNumber: "VIN",
        clientAccounts:[],
        accountId:""
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
               this.getAccountId(this.accountNumber)
               axios.post("/api/investments",{periodType: this.period, accountId: this.accountId, amount: this.amount  })
               .then(response => window.location.href = "/web/investments.html")
                .catch((error) =>{
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
            }
        },
        getAccountId: function( number){
        this.clientAccounts.forEach( (account) => {
            if (account.number == number){
               this.accountId = account.id
                }});
        }
    },

    mounted: function(){
        this.getData();
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
})