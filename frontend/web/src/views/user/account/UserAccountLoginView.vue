<template>
  <ContentField v-if="!$store.state.user.pulling_info">
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
              v-model="username"
              type="text"
              class="form-control"
              id="username"
              placeholder="请输入用户名"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              v-model="password"
              type="password"
              class="form-control"
              id="password"
              placeholder="请输入密码"
            />
          </div>
          <div v-if="error_message" class="alert alert-danger" role="alert">
            {{ error_message }}
          </div>
          <button type="submit" class="btn btn-primary">提交</button>
          <div class="mt-3 text-sm">
            <span class="fs-6"
              >还没有账号？
              <router-link
                :to="{ name: 'user_account_register' }"
                class="text-primary"
                >点击注册</router-link
              >
            </span>
          </div>
        </form>
      </div>
    </div>
  </ContentField>
</template>
<script>
import ContentField from "@/components/ContentField";
import { useStore } from "vuex";
import { ref } from "vue";
import router from "@/router/index";
export default {
  components: {
    ContentField,
  },
  setup() {
    const store = useStore();
    let username = ref("");
    let password = ref("");
    let error_message = ref("");

    //若已经登录过，则获取信息跳转到首页
    const jwt_token = localStorage.getItem("jwt_token");
    if (jwt_token) {
      store.commit("updateToken", jwt_token);
      store.dispatch("get_info", {
        success() {
          router.push({ name: "home_index" });
        },
        error() {},
      });
    } else {
      //若不存在token，则无需拉取用户信息，则会显示登录注册页面
      store.commit("updatePullingInfo", false);
    }

    const login = () => {
      error_message.value = "";
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        success(data) {
          console.log("登陆成功: " + JSON.stringify(data));
          store.dispatch("get_info", {
            success(info) {
              console.log("用户信息获取成功: " + JSON.stringify(info));
              router.push({ name: "home_index" });
            },
            error(info) {
              console.log("用户信息获取失败: " + JSON.stringify(info));
              error_message.value = data.error_message;
            },
          });
        },
        error(data) {
          console.log("登录失败: " + JSON.stringify(data));
          error_message.value = data.error_message;
        },
      });
    };
    // 返回组件的响应式数据和函数
    return {
      username,
      password,
      error_message,
      login,
    };
  },
};
</script>
<style scoped>
button {
  width: 100%;
}
div.error-message {
  color: red;
}
</style>
