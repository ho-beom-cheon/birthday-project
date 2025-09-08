<template>
  <div class="main-container">
    <h1>🎉 Happy Birthday! 🎂</h1>
    <p>생일 축하해! 🥳</p>
    <div class="photo-gallery">
      <!-- public 폴더나 src/assets에 사진을 넣고 경로를 수정하세요. -->
      <!-- 예: <img src="/photos/photo1.jpg" alt="추억 1"> -->
      <p>여기에 사진들을 추가하세요.</p>
    </div>
    <router-link to="/" class="back-link">방명록으로 돌아가기</router-link>
  </div>
</template>

<script setup>
// 사진 데이터나 로직을 여기에 추가할 수 있습니다.
</script>

<style scoped>
.main-container {
  text-align: center;
  padding: 2rem;
}
.photo-gallery {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1rem;
  margin: 2rem 0;
  min-height: 200px; /* 사진이 없을 때를 위한 최소 높이 */
}
.photo-gallery img {
  max-width: 300px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}
.back-link {
  color: #42b983;
}
</style>
