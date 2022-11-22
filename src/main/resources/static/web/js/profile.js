var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        errorToats: null,
        errorMsg: null,
        showEditProfile: false,
        hide_currentPass: false,
        hide_NewPass: false,
        currentPass:"",
        newPass:"",
        addressModel: null,
        phoneModel: null,
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current/profile")
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;
                if(this.clientInfo.address != null){
                    this.addressModel = this.clientInfo.address;
                }
                if(this.clientInfo.phone != null){
                    this.phoneModel = this.clientInfo.phone;
                }
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
            this.hide_currentPass = !this.hide_currentPass;
        },
        newPasswordToggle: function(){
            this.hide_NewPass = !this.hide_NewPass;
        },
        saveData: function(){
            axios.post("/api/clients/current/profile",{password: this.currentPass, newPassword: this.newPass, address: this.addressModel, phone: this.phoneModel  })
           .then(response => {
           this.showEditProfile= false;
           window.location.href = "/web/profile.html";
           })
            .catch((error) =>{
                this.errorMsg = error.response.data;
                this.errorToats.show();
            })
        }
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
})