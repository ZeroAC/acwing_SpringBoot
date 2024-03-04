<template lang="">
  <ContentField>
    <div class="container mt-5">
      <div class="row justify-content-center">
        <div class="col-md-6">
          <div class="card">
            <div
              class="card-header bg-primary text-white d-flex justify-content-center align-items-center"
            >
              kob注册
            </div>
            <div class="card-body">
              <form @submit.prevent="register">
                <div class="mb-3">
                  <label for="username" class="form-label">用户名</label>
                  <input
                    type="text"
                    class="form-control"
                    id="username"
                    v-model="username"
                    required
                  />
                </div>
                <div class="mb-3">
                  <label for="password" class="form-label">密码</label>
                  <input
                    type="password"
                    class="form-control"
                    id="password"
                    v-model="password"
                    required
                  />
                </div>
                <div class="mb-3">
                  <label for="confirmedPassword" class="form-label"
                    >确认密码</label
                  >
                  <input
                    type="password"
                    class="form-control"
                    id="confirmedPassword"
                    v-model="confirmedPassword"
                    required
                  />
                </div>
                <div
                  v-if="error_message"
                  class="alert alert-danger"
                  role="alert"
                >
                  {{ error_message }}
                </div>
                <button
                  v-if="!showSuccessMessage"
                  type="submit"
                  class="btn btn-primary"
                  style="width: 100%"
                >
                  注册
                </button>
                <div v-if="showSuccessMessage" class="alert alert-success">
                  注册成功！即将跳转到登录页面...
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </ContentField>
</template>
<script setup>
import ContentField from "@/components/ContentField";
import { useStore } from "vuex";
import { ref } from "vue";
import router from "@/router/index";
const store = useStore();
const username = ref("");
const password = ref("");
const confirmedPassword = ref("");
const error_message = ref("");
const showSuccessMessage = ref(false);
const register = () => {
  error_message.value = "";
  store.dispatch("user/register", {
    username: username.value,
    password: password.value,
    confirmedPassword: confirmedPassword.value,
    success(data) {
      console.log("注册成功: " + JSON.stringify(data));
      // 注册成功
      showSuccessMessage.value = true; // 显示注册成功信息
      // 设置一个定时器，在显示成功信息几秒后跳转到登录页面
      setTimeout(() => {
        router.push({ name: "user_account_login" });
      }, 2000); // 3秒后跳转
    },
    error(data) {
      console.log("注册失败: " + JSON.stringify(data));
      error_message.value = data.error_message;
    },
  });
};
</script>
<style scoped></style>
