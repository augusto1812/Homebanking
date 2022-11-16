var app = new Vue({
    el:"#app",
    data:{
        errorToats: null,
        errorMsg: null,
        clientInvestments:[],
    },
    created:function(){
    this.getData();
    },
    methods:{
       getData: function(){
           axios.get("/api/investments")
           .then((response) => {
               this.clientInvestments = response.data;
               console.log(this.clientInvestments);
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
        }
    },
    mounted: function(){
        this.getData();
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
})