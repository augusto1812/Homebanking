Vue.component('sidebar',{
    props: {
        name: String
    },
     data: function(){
        return{
            clientInfo: {},
            errorToats: null,
            errorMsg: null,
        }
     },
    methods: {
        getData: function(){
                axios.get("/api/clients/current")
                .then((response) => {
                    console.log("getData")
                    //get client ifo
                    this.clientInfo = response.data;
                    console.log(this.clientInfo)
                })
                .catch((error)=>{
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
            },
            signOut: function(){
                axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(() =>{
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
               })
          },
    },
        mounted: function(){
            this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
            this.getData();
        },
    template : `<div class="col-12 col-md-2 col-lg-2 ">
                                <div class="bg-light">
                                    <a href="/web/accounts.html"
                                       class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
                                        <img class="menu-logo" src="img/logoTuki.png">
                                    </a>
                                    <hr>
                                    <ul class="nav nav-pills flex-column mb-auto">
                                        <li class="nav-item">
                                            <a href="/web/accounts.html" class="nav-link link-dark">
                                                <i class="bi bi-inboxes"></i>
                                                Accounts
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/web/cards.html" class="nav-link link-dark">
                                                <i class="bi bi-credit-card"></i>
                                                Cards
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/web/transfers.html" class="nav-link link-dark">
                                                <i class="bi bi-box-arrow-right"></i>
                                                Transfers
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/web/loan-application.html" class="nav-link link-dark">
                                                <i class="bi bi-cash"></i>
                                                Loans
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/web/investments.html" class="nav-link link-dark">
                                                <i class="bi bi-bar-chart"></i>
                                                Investments
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/web/benefits.html" class="nav-link link-dark">
                                                <i class="bi bi-percent"></i>
                                                Benefits
                                            </a>
                                        </li>
                                        <li>
                                            <a href="/web/buy-sale-currency.html" class="nav-link link-dark">
                                                <i class="bi bi-currency-exchange"></i>
                                                Exchanges
                                            </a>
                                        </li>
                                    </ul>
                                    <hr>

                                <div class="dropdown">
                                    <a href="#" class="d-flex align-items-center link-dark text-decoration-none dropdown-toggle"
                                       id="dropdownUser2" data-bs-toggle="dropdown" aria-expanded="false">
                                        <img :src='"https://avatars.dicebear.com/api/adventurer-neutral/"+this.clientInfo.email+".svg"' alt="" width="32" height="32" class="rounded-circle me-2">
                                        <strong> Hola {{ this.clientInfo.firstName }}</strong>
                                    </a>
                                    <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdownUser2">
                                        <li><a class="dropdown-item" href="/web/profile.html">Profile</a></li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li>
                                        <a type="button" class="dropdown-item btn btn-outline-danger img-fluid" v-on:click="signOut" >Sign out</a>
                                        </li>
                                    </ul>
                                </div>
                                </div>
                            </div> `,

  });


//<ul class="nav nav-pills flex-column mb-auto">
//    <button class="btn btn-danger img-fluid" v-on:click="signOut">Sign out</button>
//</ul>