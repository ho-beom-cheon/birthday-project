<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
// App.vue에서는 이제 특정 컴포넌트를 import할 필요가 없습니다.
// Vue Router가 URL에 맞는 컴포넌트를 <router-view>에 렌더링합니다.
</script>

<style>
/* 전역 스타일을 여기에 추가할 수 있습니다. */
body {
  margin: 0;
  background-color: #f9f9f9;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
</style>
