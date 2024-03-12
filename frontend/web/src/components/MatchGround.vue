<template>
  <div class="match-ground container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-6 text-center mb-4">
        <div class="user-photo">
          <img
            :src="$store.state.user.photo"
            alt="用户照片"
            class="img-fluid rounded-circle"
          />
        </div>
        <div class="user-username mt-2">
          <h3>{{ $store.state.user.username }}</h3>
        </div>
      </div>

      <div class="col-md-6 text-center mb-4">
        <div class="user-photo">
          <img
            :src="$store.state.pk.opponentPhoto"
            alt="对手照片"
            class="img-fluid rounded-circle"
          />
        </div>
        <div class="user-username mt-2">
          <h3>{{ $store.state.pk.opponentUsername }}</h3>
        </div>
      </div>

      <div class="col-12 d-flex justify-content-center">
        <button
          @click="click_match_btn"
          type="button"
          class="btn btn-primary btn-lg mt-2"
        >
          {{ match_btn_info }}
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { useStore } from "vuex";
import { ref } from "vue";
const store = useStore();
let match_btn_info = ref("开始匹配");
store.commit("pk/updateOpponent", {
  username: "我的对手",
  photo:
    "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
});

const click_match_btn = () => {
  if (match_btn_info.value === "开始匹配") {
    match_btn_info.value = "取消";
    store.state.pk.socket.send(
      JSON.stringify({
        event: "start-matching",
      })
    );
  } else {
    match_btn_info.value = "开始匹配";
    store.state.pk.socket.send(
      JSON.stringify({
        event: "stop-matching",
      })
    );
  }
};
</script>
<style scoped>
div.match-ground {
  width: 60vw;
  height: 70vh;
  background-color: rgba(6, 26, 28, 0.2);
  margin: 40px auto;
}
div.user-photo {
  text-align: center;
  padding-top: 10vh;
}

div.user-photo > img {
  border-radius: 50%;
  width: 20vh;
}
div.user-username {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: white;
  padding-top: 2vh;
}
</style>
