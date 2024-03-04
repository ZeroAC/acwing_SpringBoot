<template lang="">
  <PlayGround> </PlayGround>
</template>
<script setup>
import PlayGround from "@/components/PlayGround";
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
const store = useStore();
//组件挂载后创建socket
onMounted(() => {
  store.dispatch("pk/createSocket");
});
//组件销毁前关闭socket
onUnmounted(() => {
  store.state.pk.socket.close();
});
</script>
<style lang=""></style>
