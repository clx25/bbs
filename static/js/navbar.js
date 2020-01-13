        axios.defaults.baseURL = '/bbs'
    var vm=new Vue({
        el:"#app",
        data:{
            boardList:"",
            simpleUserInfo:"",
            isAuthenticated:false,
            msg:"",
            user:""
        },
        created(){
            // this.signin()
            this.getBoard()
            
        },
        methods:{
            getBoard(){
                axios.get("/board").then(
                    result=>{
                        console.log(result)
                        this.boardList=result.data.boardList
                        this.userId=result.data.userId
                        this.simpleUserInfo=result.data.user
                        this.user=result.data.user
                }
                )
            },
            signin(){
                axios.get("/user", {
                }).then(result => {
                    if(result.data.status==200){
                        
                    }
                })
            },
            logout(){
                axios.get("/logout", {
                }).then(result => {
                    this.user=""
                
            })
            }
        }
    })