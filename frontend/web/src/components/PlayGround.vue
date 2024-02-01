<template lang="">
  <div class="playground">
    <game-map></game-map>
  </div>
</template>
<script>
import GameMap from "@/components/GameMap";
export default {
  components: {
    GameMap,
  },
};
</script>
<style scoped>
.playground {
  width: 60vw;
  height: 80vh;
  background-color: rgb(21, 235, 250);
  margin: 5vh auto;
}
</style>
