<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
      Draw
    </div>
    <div
      class="result-board-text"
      v-else-if="$store.state.pk.gameResult === 'lose'"
    >
      Lose
    </div>
    <div class="result-board-text" v-else>Win</div>
    <div class="result-board-btn">
      <button @click="restart" type="button" class="btn btn-warning btn-lg">
        再来!
      </button>
    </div>
  </div>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();

const restart = () => {
  store.commit("pk/updateStatus", "matching");
  store.commit("pk/updateGameResult", "none");
  store.commit("pk/updateOpponent", {
    username: "我的对手",
    photo:
      "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
  });
};
</script>

<style scoped>
div.result-board {
  height: 30vh;
  width: 30vw;
  background-color: rgba(50, 50, 50, 0.5);
  position: absolute;
  top: 20vh;
  left: 35vw;
}

div.result-board-text {
  text-align: center;
  color: white;
  font-size: 50px;
  font-weight: 600;
  font-style: italic;
  padding-top: 5vh;
}

div.result-board-btn {
  padding-top: 7vh;
  text-align: center;
}
</style>
