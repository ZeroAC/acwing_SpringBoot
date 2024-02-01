<template>
  <NavBar />
  <router-view />
</template>
<script>
import NavBar from './components/NavBar.vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
export default {
  components: {
    NavBar
  }
}
</script>
<style>
body {
  background: linear-gradient(to right, #ff7e5f, #feb47b);
  background-size: cover;
  background-position: center;
  /* Centers the background image */
  position: fixed;
  /* Keeps the element positioned at the same place even when scrolling */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;

  /* Ensures the element stays behind other elements with a higher z-index value */
}
</style>
