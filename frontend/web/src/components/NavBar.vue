<template>
  <nav class="navbar navbar-expand-lg bg-dark navbar-dark">
    <div class="container-fluid">
      <router-link
        class="navbar-brand"
        active-class="active-link"
        :to="
          $store.state.user.is_login
            ? { name: 'home_index' }
            : { name: 'user_account_login' }
        "
        >King Of Bots</router-link
      >
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarText"
        aria-controls="navbarText"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto ms-5 mb-2 mb-lg-0">
          <li class="nav-item me-3">
            <router-link
              class="nav-link"
              active-class="active-link"
              :to="{ name: 'pk_index' }"
              aria-current="page"
              >对战</router-link
            >
          </li>
          <li class="nav-item me-3">
            <router-link
              class="nav-link"
              active-class="active-link"
              :to="{ name: 'record_index' }"
              aria-current="page"
              >对局列表</router-link
            >
          </li>
          <li class="nav-item me-3">
            <router-link
              class="nav-link"
              active-class="active-link"
              :to="{ name: 'ranklist_index' }"
              aria-current="page"
              >排行榜</router-link
            >
          </li>
        </ul>
        <ul class="navbar-nav mb-2 mb-lg-0 me-5 ms-5">
          <div class="nav-item dropdown">
            <ul class="navbar-nav" v-if="$store.state.user.is_login">
              <li class="nav-item dropdown">
                <div class="row justify-content-center">
                  <div class="col-md-6">
                    <div
                      class="d-flex align-items-center justify-content-center"
                    >
                      <img
                        :src="$store.state.user.photo"
                        alt=""
                        class="avatar"
                      />
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div
                      class="d-flex align-items-center justify-content-center"
                    >
                      <a
                        class="nav-link dropdown-toggle"
                        href="#"
                        id="navbarDropdown"
                        role="button"
                        data-bs-toggle="dropdown"
                        aria-expanded="false"
                      >
                        {{ $store.state.user.username }}
                      </a>
                      <ul
                        class="dropdown-menu"
                        aria-labelledby="navbarDropdown"
                      >
                        <li>
                          <router-link
                            class="dropdown-item"
                            :to="{ name: 'user_bot_index' }"
                            >我的Bot</router-link
                          >
                        </li>
                        <li><hr class="dropdown-divider" /></li>
                        <li>
                          <a class="dropdown-item" href="#" @click="logout"
                            >退出</a
                          >
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
            <ul class="navbar-nav" v-else>
              <li class="nav-item">
                <router-link
                  class="nav-link"
                  :to="{ name: 'user_account_login' }"
                  role="button"
                >
                  登录
                </router-link>
              </li>
              <li class="nav-item">
                <router-link
                  class="nav-link"
                  :to="{ name: 'user_account_register' }"
                  role="button"
                >
                  注册
                </router-link>
              </li>
            </ul>
          </div>
        </ul>
      </div>
    </div>
  </nav>
</template>
<script>
import { useStore } from "vuex";
import router from "@/router/index";
export default {
  setup() {
    const store = useStore();
    const logout = () => {
      store.dispatch("logout");
      router.push({ name: "user_account_login" });
    };
    return {
      logout,
    };
  },
};
</script>
<style scoped>
.active-link {
  color: rgb(30, 237, 252); /* 设置高亮颜色 */
  font-weight: bold; /* 设置字体加粗 */
}
.avatar {
  /* 将图像强制为圆形 */
  border-radius: 50%;

  /* 设置图像的高度和宽度和字体的行高一致 */
  height: 8vh;

  /* 对于图像，可能需要添加 object-fit 来确保它们正确缩放而不是变形 */
  object-fit: cover;
}
</style>
