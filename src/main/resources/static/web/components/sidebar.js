Vue.component('sidebar',{
    props: {
        name: String
    },
    methods: {
            signOut: function(){
                axios.post('/api/logout')
                .then(response => window.location.href="/web/index.html")
                .catch(() =>{
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
            },
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
                                        <a href="/web/profile.html" class="nav-link link-dark">
                                            <i class="bi bi-person"></i>
                                            Profile
                                        </a>
                                    </li>
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
                                        <img src="https://github.com/mdo.png" alt="" width="32" height="32" class="rounded-circle me-2">
                                        <strong> nombre</strong>
                                    </a>
                                    <ul class="dropdown-menu text-small shadow" aria-labelledby="dropdownUser2">

                                        <li><a class="dropdown-item" href="#">Profile</a></li>
                                        <li>
                                            <hr class="dropdown-divider">
                                        </li>
                                        <li><a class="dropdown-item btn btn-danger img-fluid" v-on:click="signOut" >Sign out</a></li>
                                    </ul>
                                </div>

                                </div>
                            </div> `,

  });


//<ul class="nav nav-pills flex-column mb-auto">
//    <button class="btn btn-danger img-fluid" v-on:click="signOut">Sign out</button>
//</ul>