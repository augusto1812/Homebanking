var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        errorToats: null,
        errorMsg: null,
        showEditProfile: false,
        show_hide_password: false,
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current/profile")
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
        showEditToogle: function(){
            this.showEditProfile = !this.showEditProfile;
        },
        passwordToggle: function(){
            this.show_hide_password = !this.show_hide_password
        },
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
})