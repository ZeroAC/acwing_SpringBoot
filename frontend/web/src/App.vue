<template>
  <div>
    <ul>
      <li v-for="item in bot_data" :key="item.rating">{{ item.name }}:{{ item.rating }}</li>
    </ul>
  </div>
  <router-view />
</template>
<script>
import $ from 'jquery';
import { ref } from 'vue';
export default {
  name: 'App',
  setup() {
    let bot_data = ref('');
    $.ajax({
      url: 'http://127.0.0.1:3000/pk/getBotInfo',
      type: 'get',
      success: function (resp) {
        console.log(resp);
        // Assigning the response to bot_data.value to update the reactive data property
        // This ensures that the UI components relying on bot_data will re-render with the new data
        bot_data.value = resp;
      }
    })
    return {
      bot_data
    };
  }
}
</script>
<style scoped>
ul {
  list-style-type: none;
  padding: 0;
  text-align: left;
}

li {
  margin: 0.5rem 0;
  padding: 0.5rem;
  background-color: #f3f3f3;
  border-radius: 4px;
  transition: background-color 0.3s ease;
  font-size: 20px;
  font-weight: bold;
}

li:hover {
  background-color: #e1e1e1;
}
</style>
