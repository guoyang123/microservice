 import Vue from 'vue'
 import  VueRouter from 'vue-router'
 import  Custom from '@/components/Custom.vue'
 import User from '@/components/User.vue'
  import  Home from '@/components/Home.vue'
   import  Nav from '@/components/Nav.vue'
   import  Header from '@/components/Header.vue'
 // 全局注册路由   
 Vue.use(VueRouter)
 
export default  new VueRouter({
 	routes:[
		{path:"/test/:id",component:Custom,
		name:"test",
		children:[
			{path:"user/:userid",component:User,props:true} 
		]},
		{
			path:"/home",
			component: Home
			// components:{
			// 	nav: Nav,
			// 	header: Header
			// }
		}
	]
 })