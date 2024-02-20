<template>
  <ContentField>
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
