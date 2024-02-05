<template lang="">
  <div class="playground row">
    <div class="col-sm-10">
      <game-map :key="componentKey"></game-map>
    </div>
    <div class="col-sm-2 d-flex align-items-center justify-content-center">
      <button class="btn btn-primary" @click="refreshComponent">
        重新生成
      </button>
    </div>
  </div>
</template>
<script>
import GameMap from "@/components/GameMap";
export default {
  components: {
    GameMap,
  },
  data() {
    return {
      componentKey: 0,
    };
  },
  methods: {
    refreshComponent() {
      // 点击按钮时改变 key 的值，触发组件重新渲染
      this.componentKey++;
    },
  },
};
</script>
<style scoped>
.playground {
  width: 60vw;
  height: 80vh;
  /* background: radial-gradient(
    circle,
    rgba(8, 207, 243, 0.8),
    rgba(6, 25, 1, 0.6)
  ); */
  margin: 5vh auto;
}
</style>
