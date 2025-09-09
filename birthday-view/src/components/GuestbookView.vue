<template>
  <div class="guestbook-layout">
    <div class="guestbook-container">
      <h1 class="birthday-title">HAPPY BIRTHDAY</h1>
      <router-link to="/main" class="main-link">🎉 생일 축하 메인 화면으로 가기</router-link>
      <h2>방명록</h2>
      <form @submit.prevent="addMessage" class="guestbook-form">
        <div class="form-group">
          <label for="author">이름:</label>
          <input id="author" v-model="newAuthor" type="text" required placeholder="이름을 입력하세요">
        </div>
        <div class="form-group">
          <label for="message">축하 메시지:</label>
          <textarea id="message" v-model="newMessage" required placeholder="생일 축하 메시지를 남겨주세요!"></textarea>
        </div>
        <button type="submit">남기기</button>
      </form>
    </div>

    <div class="messages-list-container">
      <h3>남겨진 축하 메시지</h3>
      <div class="sort-buttons">
        <button @click="setSortOrder('desc')" :class="{ active: sortOrder === 'desc' }">최신순</button>
        <button @click="setSortOrder('asc')" :class="{ active: sortOrder === 'asc' }">오래된순</button>
      </div>
      <transition-group name="card-list" tag="div" class="messages-grid">
        <div v-for="message in sortedMessages" :key="message.id" class="message-card">
          <div class="card-header">
            <strong>{{ message.author }}</strong>
            <span class="timestamp">{{ formatTimestamp(message.createdAt) }}</span>
          </div>
          <p class="card-body">{{ message.message }}</p>
          <div class="card-footer">
            <button @click="likeMessage(message)" class="like-button" :title="`${message.likes || 0}명이 좋아합니다`">
              ❤️ <span class="like-count">{{ message.likes || 0 }}</span>
            </button>
          </div>
        </div>
      </transition-group>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import guestbookService from '../services/guestbookService';

const messages = ref([]);
const newAuthor = ref('');
const newMessage = ref('');
const sortOrder = ref('desc'); // 'desc' for newest, 'asc' for oldest

const fetchMessages = async () => {
  try {
    const response = await guestbookService.getMessages();
    messages.value = response.data;
  } catch (error) {
    console.error('메시지를 불러오는 데 실패했습니다:', error);
    alert('메시지 목록을 불러오는 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
  }
};

const sortedMessages = computed(() => {
  return [...messages.value].sort((a, b) => {
    const dateA = new Date(a.createdAt);
    const dateB = new Date(b.createdAt);
    if (sortOrder.value === 'asc') {
      return dateA - dateB; // Oldest first
    } else {
      return dateB - dateA; // Newest first
    }
  });
});

const setSortOrder = (order) => {
  sortOrder.value = order;
};

const addMessage = async () => {
  if (!newAuthor.value.trim() || !newMessage.value.trim()) {
    alert('이름과 메시지를 모두 입력해주세요.');
    return;
  }
  try {
    await guestbookService.createMessage({
      author: newAuthor.value,
      message: newMessage.value
    });
    newAuthor.value = '';
    newMessage.value = '';
    await fetchMessages(); // 새 메시지 등록 후 목록 새로고침
  } catch (error) {
    console.error('메시지를 등록하는 데 실패했습니다:', error);
    alert('메시지 등록에 실패했습니다. 서버 상태를 확인해주세요.');
  }
};

const likeMessage = async (messageToLike) => {
  try {
    const response = await guestbookService.likeMessage(messageToLike.id);
    // 서버로부터 받은 최신 정보로 메시지 업데이트
    const index = messages.value.findIndex(m => m.id === messageToLike.id);
    if (index !== -1) {
      // Vue의 반응성을 위해 likes 속성을 직접 수정합니다.
      messages.value[index].likes = response.data.likes;
    }
  } catch (error) {
    console.error('좋아요를 추가하는 데 실패했습니다:', error);
    alert('좋아요 처리에 실패했습니다. 잠시 후 다시 시도해주세요.');
  }
};

const formatTimestamp = (timestamp) => {
  if (!timestamp) return '';
  return new Date(timestamp).toLocaleString('ko-KR');
};

onMounted(() => {
  fetchMessages();
});
</script>

<style scoped>
.guestbook-layout {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  width: 100%;
  max-width: 900px;
}
.guestbook-container {
  padding: 2rem;
  font-family: inherit;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 16px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.1);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.18);
  text-align: center;
}
.birthday-title {
  font-family: "Gaegu", cursive;
  font-size: 4.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  background: linear-gradient(
    -45deg,
    #e8f8a0,
    #ec82ec,
    #f09,
    #ff007f
  );
  background-size: 400% 400%;
  animation: gradient-flow 10s ease-in-out infinite;
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  -webkit-text-stroke: 1px rgba(255, 255, 255, 0.5);
}
h2,
h3 {
  color: #333;
  font-weight: 700;
  margin-bottom: 1.5rem;
}
.main-link {
  display: inline-block;
  margin-bottom: 2.5rem;
  padding: 0.75rem 1.5rem;
  background: linear-gradient(45deg, #d387dd, #e962b1);
  color: white;
  text-decoration: none;
  font-weight: bold;
  border-radius: 12px;
  transition: all 0.3s ease;
}
.main-link:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(0, 170, 255, 0.3);
  background: linear-gradient(45deg, #e962b1, #f726d4);
}
.guestbook-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  max-width: 700px;
  margin: 0 auto 3rem;
  text-align: left;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.form-group label {
  font-weight: bold;
  color: #555;
}
input, textarea {
  width: 100%;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.8);
  font-family: inherit;
  font-size: 1rem;
  color: #333;
  box-sizing: border-box;
  transition: box-shadow 0.3s ease;
}
input:focus, textarea:focus {
  outline: none;
  border-color: #eb8dc3;
  box-shadow: 0 0 0 3px rgba(0, 170, 255, 0.3);
}
textarea {
  min-height: 120px;
  resize: vertical;
}
button[type="submit"] {
  padding: 1rem;
  background: linear-gradient(45deg, #d387dd, #e962b1);
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 1.1rem;
  font-weight: 700;
  transition: all 0.3s ease;
  align-self: center;
  width: 200px;
}
button[type="submit"]:hover {
  background: linear-gradient(45deg, #e962b1, #f726d4);
  transform: translateY(-3px);
  box-shadow: 0 6px 15px rgba(0, 170, 255, 0.3);
}
.sort-buttons {
  margin-bottom: 1.5rem;
  display: flex;
  gap: 1rem;
  justify-content: center;
}
.sort-buttons button {
  background: transparent;
  border: 1px solid #fa36c9;
  color: #00aaff;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  font-family: inherit;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}
.sort-buttons button:hover {
  background: rgba(0, 170, 255, 0.1);
}
.sort-buttons button.active {
  background: #f876cd;
  color: white;
}
.messages-list-container {
  padding: 1.5rem;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 16px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.1);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.18);
  text-align: center;
}
.messages-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1rem;
  margin-top: 1.5rem;
}
.message-card {
  background: #fff;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  border-radius: 16px;
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  border-left: 5px solid #f31e93;
  gap: 1rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  text-align: left;
}
.message-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 0.75rem;
}
.card-header strong {
  color: #f371c8;
  font-size: 1.1rem;
}
.timestamp {
  font-size: 0.8rem;
  color: #999;
}
.card-body {
  color: #555;
  line-height: 1.6;
  margin: 0;
}

.card-footer {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.like-button {
  background-color: #fff0f5;
  border: 1px solid #ffc0cb;
  color: #db7093; /* PaleVioletRed */
  padding: 0.4rem 0.8rem;
  border-radius: 20px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  transition: all 0.2s ease-in-out;
}

.like-button:hover, .like-button:focus {
  background-color: #ffdde5;
  transform: scale(1.05);
  outline: none;
}
.like-count {
  font-weight: bold;
}

.card-list-enter-active,
.card-list-leave-active {
  transition: all 0.5s ease;
}
.card-list-enter-from,
.card-list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}
.card-list-move {
  transition: transform 0.5s ease;
}

@keyframes gradient-flow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

@media (max-width: 768px) {
  .guestbook-layout {
    gap: 1.5rem;
  }
  .guestbook-container,
  .messages-list-container {
    padding: 1.5rem 1rem;
  }
  .birthday-title {
    font-size: 3rem;
  }
  .main-link {
    padding: 0.6rem 1.2rem;
    font-size: 0.9rem;
    margin-bottom: 2rem;
  }
  .guestbook-form {
    gap: 1rem;
  }
  button[type="submit"] {
    width: 100%;
  }
  .messages-grid {
    grid-template-columns: 1fr;
  }
}
</style>
