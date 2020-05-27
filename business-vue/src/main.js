import Vue from 'vue'
  import App from './App.vue'
  import router from '@/router/router.js'
import  axios from 'axios' 
import Vuex from 'vuex'

Vue.config.productionTip = false

Vue.use(axios)
Vue.use(Vuex)
// true:可以携带cookie
axios.defaults.withCredentials=true 

const store=new Vuex.Store({
	state:{
		userInfo:{}
	},
	mutations:{
		setUserInfo(state,user){
			state.userInfo=user
		}
	}
})

new Vue({
	router,
	store,
  render: h => h(App)
 
}).$mount('#app')




