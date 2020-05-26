import Vue from 'vue'
  import App from './App.vue'
 import  Custom from './components/Custom.vue'
 import User from './components/User.vue'
 import  VueRouter from 'vue-router'
 // 全局注册路由
 Vue.use(VueRouter)

Vue.config.productionTip = false

const routes=[	
	{path:"/test/:id",component:Custom,
	name:"test",
	children:[
		{path:"user/:userid",component:User}
	]}
]

// /test/10 -->Custom
// /test/10/user/1 --> User
const  router=new VueRouter({
	routes
})


new Vue({
	router,
  render: h => h(App)
 
}).$mount('#app')




