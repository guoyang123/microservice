<template>
	
	<div>
		<h1>自定义User组件</h1>
	
		路由参数传值： 	{{userid}}<!-- {{$route.params.userid}} -->
	
	<input type="text"  v-model="username" /><br/>
	<input type="password"  v-model="password" /><br/>
	 <button v-on:click="login">登录</button><br/>
	
	 <button v-on:click="cart">购物车列表</button><br/>
		<span>username: {{userInfo.username}}</span>
		<router-link to="/home">home</router-link>
	<!-- 	<router-view></router-view> -->
		<router-view name="nav" ></router-view>
		<!-- <router-view name="header" ></router-view> -->
	</div>
	
</template>

<script>
	import axios from 'axios'
	export default{
		name:"User",
		props:['userid'],
		data(){
			return{
				"username":"",
				"password":""
			}
		},
		computed:{
			// userInfo: function(){
			// 	return this.$store.state.userInfo
			// }
			userInfo:{
				get:function(){
					return this.$store.state.userInfo
				},
				set:function(v){
					this.$store.commit("setUserInfo",v)
				}
			}
		},
		methods:{
			login:function(){
				
				//请求登录接口
				console.log(this.username)
				console.log(this.password)
				
				// 保存vue实例
				const  vm=this
				axios.get("http://neuedu.com:8084/user/user/login/"+this.username+"/"+this.password)
				.then(function(res){
					console.log("=======success=========")
					console.log(res)
					if(res.status==200){
						const data=res.data;
						if(data.status==0){
							console.log(data.data.username)
							vm.userInfo=data.data
							vm.$store.commit("setUserInfo",data.data)
						}
					}
				},function(res){
					console.log("=======fail=========")
					console.log(res)
				})
			},
			cart:function(){
				
				// 保存vue实例
				const  vm=this
				axios.get("http://neuedu.com:8084/cart/cart/add/1/10")
				.then(function(res){
					console.log("=======success=========")
					console.log(res)
					if(res.status==200){
						const data=res.data;
						if(data.status==0){
							console.log(data.data)
							
							
						}
					}
				},function(res){
					console.log("=======fail=========")
					console.log(res)
				})
			}
		}
	}
</script>

<style>
</style>
