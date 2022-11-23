var app = new Vue({
    el:"#app",
    data:{
        errorToats: null,
        errorMsg: null,
        cardType:"none",
        cardColor:"none",
        accountNumber:"",
        clientInfo: {}
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
            if(this.cardType == "none" || this.cardColor == "none" || this.accountNumber == ""){
                this.errorMsg = "You must select a card type, color and account";
                this.errorToats.show();
            }else{
                axios.post(`/api/clients/current/cards?cardType=${this.cardType}&cardColor=${this.cardColor}&accountNumber=${this.accountNumber}`)
                .then(response => window.location.href = "/web/cards.html")
                .catch((error) =>{
                    this.errorMsg = error.response.data;  
                    this.errorToats.show();
                })
            }
        },
    },
    mounted: function(){
        this.getData();
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
})