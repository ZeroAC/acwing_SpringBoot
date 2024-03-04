import axios from "axios"; // 从 axios 库中导入 axios，用于发起 HTTP 请求

// 定义 API 的基础 URL，所有的请求都会使用这个 URL 作为起始点
let base_url = "http://127.0.0.1:3000/user/account/";

// 使用 export default 导出 Vuex store 对象
export default {
  namespaced: true,
  // state 对象包含了该模块的状态
  state: {
    id: "", // 用户的 ID
    username: "", // 用户的用户名
    password: "", // 用户的密码
    photo: "", // 用户的照片
    token: "", // 认证后分配给用户的 token
    is_login: false, // 用户是否登录的标志
    pulling_info: true, //是否正在拉取用户信息
  },
  // getters 对象包含了多个获取 state 中数据的方法
  getters: {
    // 获取用户ID
    getUserId(state) {
      return state.id;
    },
    // 获取用户名
    getUsername(state) {
      return state.username;
    },
    // 获取用户照片
    getUserPhoto(state) {
      return state.photo;
    },
    // 获取用户token
    getUserToken(state) {
      return state.token;
    },
    // 判断用户是否登录
    isUserLoggedIn(state) {
      return state.is_login;
    },
  },
  // mutations 对象包含了多个直接修改 state 的方法
  mutations: {
    // updateUser 是一个 mutation，用于更新用户信息
    updateUser(state, user) {
      state.id = user.id;
      state.username = user.username;
      state.photo = user.photo;
      state.is_login = user.is_login;
    },
    // updateToken 是一个 mutation，用于更新用户的 token
    updateToken(state, token) {
      state.token = token;
    },
    logout(state) {
      state.id = "";
      state.username = "";
      state.photo = "";
      state.token = "";
      state.is_login = false;
    },
    updatePullingInfo(state, pulling_info) {
      state.pulling_info = pulling_info;
    },
  },
  // actions 对象包含了多个处理异步操作的方法
  actions: {
    register(context, data) {
      axios
        .post(base_url + "register", {
          username: data.username,
          password: data.password,
          confirmedPassword: data.confirmedPassword,
        })
        .then((response) => {
          const responseData = response.data;
          if (responseData.error_message === "success") {
            data.success(responseData);
          } else {
            data.error(responseData);
          }
        })
        .catch((error) => {
          data.error(error);
        });
    },
    // login 是一个 action，用于处理用户登录
    login(context, data) {
      axios
        .post(base_url + "token", {
          username: data.username,
          password: data.password,
        })
        .then((response) => {
          const responseData = response.data;
          if (responseData.error_message === "success") {
            // 如果登录成功，更新 token
            context.commit("updateToken", responseData.token);
            //把用户的 token 存储到浏览器的 local storage，这样就可以实现登录状态持久化
            localStorage.setItem("jwt_token", responseData.token);
            data.success(responseData);
          } else {
            data.error(responseData);
          }
        })
        .catch((error) => {
          data.error(error);
        });
    },
    get_info(context, data) {
      axios
        .get(base_url + "info", {
          headers: {
            Authorization: "Bearer " + context.state.token, // 请求头中包含 token
          },
        })
        .then((response) => {
          const responseData = response.data;
          if (responseData.error_message === "success") {
            // 如果获取信息成功，更新用户状态
            context.commit("updateUser", {
              ...responseData,
              is_login: true,
            });
            data.success(responseData);
          } else {
            data.error(responseData);
          }
        })
        .catch((error) => {
          data.error(error);
        });
    },
    logout(context) {
      localStorage.removeItem("jwt_token");
      context.commit("logout");
    },
  },
  // modules 对象允许将 store 分割成模块，每个模块拥有自己的 state、mutations、actions、getters
  modules: {},
};
