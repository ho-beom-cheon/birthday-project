import { createRouter, createWebHistory } from 'vue-router';
import GuestbookView from '../components/GuestbookView.vue';
import MainView from '../views/MainView.vue';

const routes = [
  {
    path: '/',
    name: 'Guestbook',
    component: GuestbookView,
  },
  {
    path: '/main',
    name: 'Main',
    component: MainView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
