<template>
	
	<div id="app1">
		<h1>自定义组件</h1>
		{{username}}
		路由参数传值： {{$route.params.id}}
		<button v-on:click="goBack">后退</button>
		
		<router-view></router-view>
		
	</div>
	
</template>

<script>
	
	export default{
		name:"Custom",
		data(){
			return{
				"username":"zhangsan"
			}
		},
		methods:{
			goBack:function(){
				//this.$router.go(-1)
				this.$router.push("/")
			}
		}
	}
</script>

<style>
</style>
