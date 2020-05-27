<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js App"/>
	
	<span>username:{{userInfo.username}}</span>
	
	<router-link :to="{name:'test',params:{id:100}}">go to custom</router-link>
	<router-view></router-view>
	
  </div>

</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    HelloWorld
  },
  computed:{
  userInfo:{
  	get:function(){
  		return this.$store.state.userInfo
  	},
  	set:function(v){
  		this.$store.commit("setUserInfo",v)
  	}
  }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
