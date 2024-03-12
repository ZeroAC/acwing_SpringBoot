const BASE_URL = "ws://127.0.0.1:3000/websocket/";

export default {
  namespaced: true,
  state: {
    socket: null, //socket连接
    opponentUsername: "", //对手名
    opponentPhoto: "", //对手头像
    status: "matching", //matching表示匹配界面，playing表示对战界面
    gameMap: null, //对战界面的地图数据
    id: 0, //我方游戏信息 id 起点
    sx: 0,
    sy: 0,
    opponentId: 0, //对手信息
    opponentSx: 0,
    opponentSy: 0,
    gameObject: null, // 对局信息 两条蛇
    gameResult: "none", // all、win、lose
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
    updateGame(state, game) {
      state.gameMap = game.map;
      state.id = game.id;
      state.sx = game.sx;
      state.sy = game.sy;
      state.opponentId = game.opponentId;
      state.opponentSx = game.opponentSx;
      state.opponentSy = game.opponentSy;
    },
    updateStatus(state, status) {
      state.status = status;
    },
    closeSocket(state) {
      state.socket = null;
      console.log("WebSocket连接已关闭");
    },
    updateGameObject(state, gameObject) {
      state.gameObject = gameObject;
    },
    updateGameResult(state, gameResult) {
      state.gameResult = gameResult;
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
        console.log("后端发来的socket消息:", msg.data);
        const data = JSON.parse(msg.data);
        if (data.event === "start-matching") {
          //匹配成功
          commit("updateOpponent", {
            username: data.opponentUsername,
            photo: data.opponentPhoto,
          });
          commit("updateGame", data.game);
          //匹配成功两秒后跳转到对战界面
          setTimeout(() => {
            commit("updateStatus", "playing");
          }, 2000);
        } else if (data.event === "move") {
          //从后端接收到两者的移动信息 则开始移动
          const game = rootState.pk.gameObject;
          const [snake0, snake1] = game.snakes;
          snake0.set_direction(data.a_direction);
          snake1.set_direction(data.b_direction);
          console.log(
            `本蛇移动方向: ${data.a_direction}, 对手移动方向: ${data.b_direction}`
          );
        } else if (data.event === "result") {
          const game = rootState.pk.gameObject;
          const gameResult = data.gameResult;
          const [snake0, snake1] = game.snakes;
          console.log(`output->gameResult`, gameResult);

          if (gameResult === "all" || gameResult === "lose") {
            snake0.status = "die";
          }
          if (data.loser === "all" || gameResult === "win") {
            snake1.status = "die";
          }
          commit("updateGameResult", data.gameResult);
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
