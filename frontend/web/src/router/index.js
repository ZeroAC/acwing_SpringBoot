import { createRouter, createWebHistory } from "vue-router";
import { useStore } from "vuex";
// 把一些额外信息放到一个额外的域里面，meta信息里面存一下是否要授权，
// 如果需要授权而且没有登录，重定向到登录页面，重定向到登录界面。
const routes = [
  {
    path: "/",
    name: "home_index",
    redirect: "/pk",
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/pk",
    name: "pk_index",
    component: () => import("@/views/pk/PKIndexView"),
    meta: {
      requiresAuth: true,
    },
  },

  {
    path: "/record",
    name: "record_index",
    component: () => import("@/views/record/RecordIndexView"),
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/ranklist",
    name: "ranklist_index",
    component: () => import("@/views/ranklist/RankListIndexView"),
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/user/bot",
    name: "user_bot_index",
    component: () => import("@/views/user/bot/UserBotIndexView"),
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: () => import("@/views/user/account/UserAccountLoginView"),
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: () => import("@/views/user/account/UserAccountRegisterView"),
    meta: {
      requiresAuth: false,
    },
  },
  {
    path: "/404/",
    name: "404",
    component: () => import("@/views/error/NotFoundView"),
    meta: {
      requiresAuth: true,
    },
  },
  {
    path: "/:catchAll(.*)",
    name: "not_match",
    redirect: "/404",
    meta: {
      requiresAuth: true,
    },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});
router.beforeEach((to, from, next) => {
  const store = useStore();
  // 检查用户是否已经登录，这里假设登录状态保存在Vuex的store中
  const isLoggedIn = store.getters["user/isUserLoggedIn"];

  // 检查目标路由是否需要认证
  const requiresAuth = to.matched.some((record) => record.meta.requiresAuth);

  if (requiresAuth && !isLoggedIn) {
    // 如果需要认证且用户未登录，跳转到登录页面
    next({ name: "user_account_login" });
  } else if (
    (to.name === "user_account_login" || to.name === "user_account_register") &&
    isLoggedIn
  ) {
    // 如果目标是登录或注册页但用户已登录，重定向到首页
    next({ name: "home_index" }); // 假设已经登录的用户应该被重定向到的页面
  } else {
    // 否则，正常导航到目标路由
    next();
  }
});
export default router;
