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

      // 接收消息时触发
      socket.onmessage = (event) => {
        const data = JSON.parse(event.data);
        console.log("接收到消息:", data);
        // 根据接收到的消息内容更新状态
        // 例如，如果消息中包含对手的信息:
        // commit('updateOpponent', { username: data.username, photo: data.photo });
      };

      // 出现错误时触发
      socket.onerror = (event) => {
        console.error("WebSocket发生错误:", event);
        // 根据需要处理错误
      };

      // 连接关闭时触发
      socket.onclose = () => {
        commit("closeSocket");
      };
      // 注意：在实际项目中，应该处理连接失败的情况
    },
  },
};
