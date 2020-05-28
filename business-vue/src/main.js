import Vue from 'vue'
  import App from './App.vue'
  import router from '@/router/router.js'
import  axios from 'axios' 
import Vuex from 'vuex'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.config.productionTip = false

import Vant from 'vant';
import 'vant/lib/index.css';

Vue.use(Vant);
Vue.use(ElementUI);
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




