<template>
  <NavBar />
  <router-view />
</template>
<script>
import NavBar from "./components/NavBar.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.min.js";
// 引入axios
// import axios from "axios";
export default {
  components: {
    NavBar,
  },
  setup() {
    // let base_url = "http://127.0.0.1:3000/user/account/";
    // 测试LoginService
    // axios
    //   .post(base_url + "token", {
    //     username: "yxc",
    //     password: "123",
    //   })
    //   .then((response) => {
    //     console.log(response.data);
    //   })
    //   .catch((error) => {
    //     console.log(error.response);
    //   });
    // 测试InfoService
    // axios
    //   .get(base_url + "info", {
    //     headers: {
    //       Authorization:
    //         "Bearer " +
    //         "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI3NzcxODc3YzVkMDE0MzhmYjhkYjBiNGYxNjllYjNiMCIsInN1YiI6IjYiLCJpc3MiOiJzZyIsImlhdCI6MTcwODMzMzg5NiwiZXhwIjoxNzA4OTM4Njk2fQ.vkHxEylGwkSi1jIdKynKEfOQE1aFtu2kePn_66r_N4Q",
    //     },
    //   })
    //   .then((response) => {
    //     console.log(response.data);
    //   })
    //   .catch((error) => {
    //     console.log(error.response);
    //   });
    // 测试RegisterService
    // axios
    //   .post(base_url + "register", {
    //     username: "jcc",
    //     password: "123456",
    //     confirmedPassword: "123456",
    //   })
    //   .then((response) => {
    //     console.log(response.data);
    //   })
    //   .catch((error) => {
    //     console.log(error.response);
    //   });
  },
};
</script>
<style>
body {
  background: linear-gradient(to right, #ff7e5f, #feb47b);
  background-size: cover;
  background-position: center;
  /* Centers the background image */
  position: fixed;
  /* Keeps the element positioned at the same place even when scrolling */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;

  /* Ensures the element stays behind other elements with a higher z-index value */
}
</style>
