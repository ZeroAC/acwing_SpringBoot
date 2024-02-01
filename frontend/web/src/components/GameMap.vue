<template lang="">
  <div class="game-map" ref="parent">
    <canvas ref="ctx"></canvas>
  </div>
</template>
<script>
import GameMap from "@/assets/scripts/GameMap";
import { ref, onMounted } from "vue";
export default {
  setup() {
    const ctx = ref(null);
    const parent = ref(null);

    onMounted(() => {
      if (ctx.value && parent.value) {
        console.log(ctx.value.getContext("2d"));
        new GameMap(ctx.value.getContext("2d"), parent.value);
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
