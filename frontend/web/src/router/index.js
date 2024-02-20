import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/",
    name: "home_index",
    redirect: "/pk",
  },
  {
    path: "/pk",
    name: "pk_index",
    component: () => import("@/views/pk/PKIndexView"),
  },

  {
    path: "/record",
    name: "record_index",
    component: () => import("@/views/record/RecordIndexView"),
  },
  {
    path: "/ranklist",
    name: "ranklist_index",
    component: () => import("@/views/ranklist/RankListIndexView"),
  },
  {
    path: "/user/bot",
    name: "user_bot_index",
    component: () => import("@/views/user/bot/UserBotIndexView"),
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: () => import("@/views/user/account/UserAccountLoginView"),
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: () => import("@/views/user/account/UserAccountRegisterView"),
  },
  {
    path: "/404/",
    name: "404",
    component: () => import("@/views/error/NotFoundView"),
  },
  {
    path: "/:catchAll(.*)",
    name: "not_match",
    redirect: "/404",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
