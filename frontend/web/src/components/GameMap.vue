<template lang="">
  <div class="game-map" ref="parent">
    <canvas ref="ctx" tabindex="0"></canvas>
  </div>
</template>
<script>
import GameMapObject from "@/assets/scripts/GameMapObject";
import { ref, onMounted } from "vue";
import { useStore } from "vuex";
export default {
  setup() {
    const ctx = ref(null);
    const parent = ref(null);
    const store = useStore();

    // 当组件挂载完成后，如果 canvas 上下文和父元素都存在，则创建一个新的 GameMapObject 实例
    onMounted(() => {
      if (ctx.value && parent.value) {
        store.commit(
          "pk/updateGameObject",
          new GameMapObject(ctx.value.getContext("2d"), parent.value, store)
        );
      }
    });

    return {
      ctx,
      parent,
    };
  },
};
</script>
<style>
.game-map {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
