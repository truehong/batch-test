import Vue from 'vue'
import VueRouter from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import GridSystem from '../views/GridSystem.vue'
import GridListPage from '../views/GridListPage.vue'
import Jobs from '../views/Jobs.vue'
import History from '../views/JobHistory.vue'

Vue.use(VueRouter) // view router 설정

const routes = [
    {
        path : '/',
        component : Dashboard
    },
    {
        path : '/grid',
        component : GridSystem
    },
    {
        path : '/grid-page',
        component : GridListPage
    },
    {
        path : '/jobs',
        component : Jobs
    },
    {
        path : '/history',
        component : History
    }
    ,
]

const router = new VueRouter({
    mode : 'history',
    routes : routes

})

export default router;