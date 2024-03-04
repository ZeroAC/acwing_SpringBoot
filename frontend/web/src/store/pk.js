const BASE_URL = "ws://127.0.0.1:3000/websocket/";

export default {
  namespaced: true,
  state: {
    socket: null, //socket连接
    opponentUsername: "", //对手名
    opponentPhoto: "", //对手头像
    status: "matching", //matching表示匹配界面，playing表示对战界面
  },
  getters: {
    socket: (state) => state.socket,
    opponentUsername: (state) => state.opponentUsername,
    opponentPhoto: (state) => state.opponentPhoto,
    status: (state) => state.status,
  },
  mutations: {
    updateSocket(state, socket) {
      state.socket = socket;
    },
    updateOpponent(state, { username, photo }) {
      state.opponentUsername = username;
      state.opponentPhoto = photo;
    },
    updateStatus(state, status) {
      state.status = status;
    },
    closeSocket(state) {
      state.socket = null;
      console.log("WebSocket连接已关闭");
    },
  },
  actions: {
    // 创建WebSocket连接
    createSocket({ commit, rootState }) {
      // 使用用户的token来构建WebSocket连接URL
      const socket = new WebSocket(BASE_URL + rootState.user.token);

      // 连接成功时触发
      socket.onopen = () => {
        console.log("WebSocket连接成功: 用户ID", rootState.user.id);
        commit("updateSocket", socket); // 更新state中的socket实例
      };

      // 接收后端消息时触发 即匹配成功两秒后跳转到对战界面
      socket.onmessage = (msg) => {
        console.log("后端发来的匹配结果:", msg.data);
        const data = JSON.parse(msg.data);
        if (data.event === "start-matching") {
          commit("updateOpponent", {
            username: data.opponentUsername,
            photo: data.opponentPhoto,
          });
          //匹配成功两秒后跳转到对战界面
          setTimeout(() => {
            commit("updateStatus", "playing");
          }, 2000);
        }
      };

      // 出现错误时触发
      socket.onerror = (event) => {
        console.error("WebSocket发生错误:", event);
        // 根据需要处理错误
      };

      // 连接关闭时触发
      socket.onclose = () => {
        commit("closeSocket");
        commit("updateStatus", "matching");
      };
      // 注意：在实际项目中，应该处理连接失败的情况
    },
  },
};
