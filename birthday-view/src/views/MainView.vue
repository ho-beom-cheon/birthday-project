<template>
  <div class="birthday-container">
    <div class="hero-section">
      <img :src="allPhotos[0]?.url" alt="Main Photo" class="hero-image" v-if="allPhotos.length > 0">
      <div class="hero-text">
        <h1 class="title">HAPPY<br>BIRTHDAY!</h1>
        <p class="subtitle">우리 막내 해인이의 생일을 축하합니다 🥳</p>
        <img :src="hamoCharacter" alt="Hamo Character" class="hamo-character" @click="launchFireworks" />
      </div>
    </div>

    <div class="section intro-section">
      <h2 class="section-title">Welcome!</h2>
      <p class="intro-text">
        해인이의 특별한 날을 축하하기 위해<br>
        만들어진 작은 공간에 오신 것을 환영합니다!<br>
        함께 즐거운 추억을 구경하고<br>
        따뜻한 축하 메시지도 남겨주세요.
      </p>
    </div><br>
    <img :src="batuma" alt="batuma" class="batuma-image">
    <h1>퇴근 바투메요~!</h1><br><br>
    <div class="section gallery-section">
      <h2 class="section-title">✨ Special Moments</h2>
      <div v-if="allPhotos.length === 0" class="no-photos-message">
        <p>아직 등록된 사진이 없어요. 😢</p>
      </div>
      <transition-group v-else name="photo-list" tag="div" class="photo-gallery">
        <div v-for="photo in displayedPhotos" :key="photo.url" class="photo-item" @click="openModal(photo)" :style="{ '--aspect-ratio': '1/1' }">
          <img :src="photo.url" :alt="photo.alt" />
        </div>
      </transition-group>
      <div v-if="hasMorePhotos" class="load-more-container">
        <button @click="loadMorePhotos" class="load-more-button">더보기</button>
      </div>
    </div>

    <div class="section link-section">
      <router-link to="/" class="guestbook-link">💌 축하 메시지 남기러 가기</router-link>
    </div>

    <!-- 사진 크게 보기 모달 -->
    <transition name="modal-fade">
      <div v-if="selectedPhoto" class="modal-overlay" @click="closeModal">
        <button class="modal-nav-button prev" @click.stop="showPrevPhoto">‹</button>
        <div class="modal-content" @click.stop>
          <img :src="selectedPhoto.url" :alt="selectedPhoto.alt" />
        </div>
        <button class="modal-nav-button next" @click.stop="showNextPhoto">›</button>
        <button class="close-button" @click="closeModal">&times;</button>
      </div>
    </transition>
    <!-- 폭죽 효과를 위한 컨테이너 -->
    <div class="fireworks-container" ref="fireworksContainer"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue';
import hamoCharacter from '../assets/image/hamo2.png';
import batuma from '../assets/image/batuma2.png';

const allPhotos = ref([]);
const loadCount = ref(9);
const visibleCount = ref(9);

const displayedPhotos = computed(() => allPhotos.value.slice(0, visibleCount.value));
const hasMorePhotos = computed(() => visibleCount.value < allPhotos.value.length);

const shuffleArray = (array) => {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
  return array;
};

const fetchPhotos = async () => {
  try {
    // 캐시를 방지하기 위해 타임스탬프를 쿼리 파라미터로 추가합니다.
    const response = await fetch(`/photos.json?t=${new Date().getTime()}`);
    if (!response.ok) {
      throw new Error('사진 데이터를 불러오는 데 실패했습니다.');
    }
    const data = await response.json();
    allPhotos.value = shuffleArray(data);
  } catch (error) {
    console.error(error);
    // 사용자에게 오류 메시지를 보여주는 로직을 추가할 수 있습니다.
  }
};

const selectedPhoto = ref(null);
const selectedPhotoIndex = ref(null);

const openModal = (photo) => {
  // 전체 사진 목록에서 인덱스를 찾습니다.
  const index = allPhotos.value.findIndex(p => p.url === photo.url);
  if (index !== -1) {
    selectedPhotoIndex.value = index;
    selectedPhoto.value = allPhotos.value[index];
  }
};

const closeModal = () => {
  selectedPhoto.value = null;
  selectedPhotoIndex.value = null;
};

const showNextPhoto = () => {
  if (selectedPhotoIndex.value === null || allPhotos.value.length === 0) return;
  const newIndex = (selectedPhotoIndex.value + 1) % allPhotos.value.length;
  selectedPhotoIndex.value = newIndex;
  selectedPhoto.value = allPhotos.value[newIndex];
};

const showPrevPhoto = () => {
  if (selectedPhotoIndex.value === null || allPhotos.value.length === 0) return;
  const newIndex = (selectedPhotoIndex.value - 1 + allPhotos.value.length) % allPhotos.value.length;
  selectedPhotoIndex.value = newIndex;
  selectedPhoto.value = allPhotos.value[newIndex];
};

const handleKeydown = (e) => {
  if (!selectedPhoto.value) return;
  if (e.key === 'ArrowRight') showNextPhoto();
  else if (e.key === 'ArrowLeft') showPrevPhoto();
  else if (e.key === 'Escape') closeModal();
};

const loadMorePhotos = () => {
  visibleCount.value += loadCount.value;
};

const fireworksContainer = ref(null);

const launchFireworks = () => {
  if (!fireworksContainer.value) return;

  const particleCount = 50;
  const colors = ['#FFD700', '#FF69B4', '#00BFFF', '#ADFF2F', '#FFFFFF'];

  for (let i = 0; i < particleCount; i++) {
    const particle = document.createElement('div');
    particle.classList.add('particle');

    const angle = Math.random() * Math.PI * 2;
    // 화면의 너비와 높이 중 작은 값을 기준으로 폭죽이 퍼지는 반경을 설정합니다.
    const blastRadius = Math.min(window.innerWidth, window.innerHeight) * (Math.random() * 0.3 + 0.2);

    particle.style.setProperty('--x', `${Math.cos(angle) * blastRadius}px`);
    particle.style.setProperty('--y', `${Math.sin(angle) * blastRadius}px`);
    particle.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
    particle.style.animationDelay = `${Math.random() * 0.2}s`;

    fireworksContainer.value.appendChild(particle);

    setTimeout(() => particle.remove(), 1200);
  }
};

const handleResize = () => {
  if (window.innerWidth <= 768) {
    loadCount.value = 4;
  } else {
    loadCount.value = 9;
  }
};

onMounted(() => {
  fetchPhotos();

  // 화면 크기에 따라 초기 사진 개수 설정
  if (window.innerWidth <= 768) {
    loadCount.value = 4;
    visibleCount.value = 4;
  } else {
    loadCount.value = 9;
    visibleCount.value = 9;
  }
  window.addEventListener('resize', handleResize);
  window.addEventListener('keydown', handleKeydown);

  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
        observer.unobserve(entry.target);
      }
    });
  }, {
    threshold: 0.1,
  });

  document.querySelectorAll('.section').forEach(section => {
    observer.observe(section);
  });
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  window.removeEventListener('keydown', handleKeydown);
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@700;900&family=Gaegu:wght@400;700&display=swap');

.birthday-container {
  font-family: 'Gaegu', cursive;
  width: 100%;
  max-width: 480px;
  margin: 0 auto;
  background: linear-gradient(to top, #fff1eb 0%, #ace0f9 100%);
  color: #333;
  overflow-x: hidden;
}

.hero-section {
  position: relative;
  text-align: center;
  padding: 4rem 2rem 2rem;
  min-height: 90vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.hero-image {
  width: 70%;
  border-radius: 20px;
  box-shadow: 0 12px 30px rgba(0,0,0,0.15);
  border: 6px solid white;
  transform: rotate(-5deg);
  margin-top: 2rem;
  transition: transform 0.6s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}
.hero-image:hover {
  transform: rotate(3deg) scale(1.05);
}

.hero-text {
  position: relative;
  z-index: 2;
}
.hero-text .title {
  font-family: 'Poppins', sans-serif;
  font-size: 4.8rem;
  font-weight: 900;
  color: #fff;
  line-height: 1;
  text-shadow: 0px 4px 0px #ffc3a0, 0px 8px 10px rgba(0,0,0,0.1);
  margin: 0 0 1rem 0;
}
.hamo-character {
  width: 140px;
  margin-top: 1rem;
  cursor: pointer;
  animation: bounce 2.5s infinite;
}
.hero-text .subtitle {
  font-size: 1.6rem;
  color: #444;
  font-weight: 700;
  margin: 2rem 0;
  background-color: rgba(255, 255, 255, 0.7);
  padding: 0.5rem 1rem;
  border-radius: 10px;
  display: inline-block;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-25px);
  }
  60% {
    transform: translateY(-15px);
  }
}

.section {
  padding: 3rem 1.5rem;
  margin: 0 1.5rem 3rem 1.5rem;
  text-align: center;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 24px;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 1s ease-out, transform 1s ease-out;
}
.section.visible {
  opacity: 1;
  transform: translateY(0);
}

.section-title {
  font-family: 'Poppins', sans-serif;
  font-size: 2.4rem;
  font-weight: 700;
  margin-bottom: 2rem;
  color: #ff8a65;
  display: inline-block;
  position: relative;
}

.intro-text {
  font-size: 1.3rem;
  line-height: 1.8;
  color: #555;
  font-weight: 700;
  max-width: 31ch;
  margin: 0 auto;
}

.intro-image {
  width: 60%;
  max-width: 250px;
  border-radius: 15px;
  box-shadow: 0 10px 20px rgba(0,0,0,0.15);
  margin: 1.5rem auto;
  display: block;
  border: 4px solid white;
}
.batuma-image {
  width: 60%;
  max-width: 300px;
  border-radius: 15px;
  margin: 1.5rem auto;
  display: block;
}
.load-more-container {
  text-align: center;
  margin-top: 2rem;
}

.load-more-button {
  display: inline-block;
  padding: 0.8rem 2rem;
  background-image: linear-gradient(to right, #6a82fb 0%, #fc5c7d 51%, #6a82fb 100%);
  background-size: 200% auto;
  color: white;
  text-decoration: none;
  border: none;
  border-radius: 50px;
  font-family: 'Gaegu', cursive;
  font-size: 1.2rem;
  font-weight: bold;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  transition: all 0.4s ease;
  cursor: pointer;
}

.load-more-button:hover {
  background-position: right center;
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
}

.photo-gallery {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
}

.photo-list-enter-active {
  transition: all 0.5s ease;
}
.photo-list-enter-from {
  opacity: 0;
  transform: translateY(20px);
}
.photo-item {
  aspect-ratio: 1 / 1;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.photo-item:hover {
  transform: scale(1.08) rotate(3deg);
  box-shadow: 0 8px 20px rgba(0,0,0,0.2);
}
.photo-gallery img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.link-section {
  background: none;
  backdrop-filter: none;
  border: none;
  box-shadow: none;
  padding-bottom: 4rem;
}
.guestbook-link {
  display: inline-block;
  padding: 1.2rem 3rem;
  background-image: linear-gradient(to right, #ff7e5f 0%, #feb47b 51%, #ff7e5f 100%);
  background-size: 200% auto;
  color: white;
  text-decoration: none;
  border-radius: 50px;
  font-size: 1.4rem;
  font-weight: bold;
  box-shadow: 0 5px 15px rgba(254, 180, 123, 0.6);
  transition: all 0.4s ease;
}
.guestbook-link:hover {
  background-position: right center;
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(254, 180, 123, 0.8);
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.9);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  cursor: pointer;
  -webkit-backdrop-filter: blur(5px);
  backdrop-filter: blur(5px);
}

.modal-content {
  position: relative;
  cursor: default;
}

.modal-content img {
  display: block;
  border-radius: 8px;
  max-width: 85vw;
  max-height: 85vh;
}

.close-button {
  position: fixed;
  top: 20px;
  right: 30px;
  background: none;
  border: none;
  color: white;
  font-size: 3.2rem;
  cursor: pointer;
  line-height: 1;
  text-shadow: 0 2px 5px rgba(0,0,0,0.5);
}

.modal-nav-button {
  position: fixed;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.3);
  color: white;
  border: none;
  font-size: 3rem;
  font-weight: bold;
  cursor: pointer;
  z-index: 1001;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
  transition: background-color 0.2s;
}
.modal-nav-button:hover {
  background-color: rgba(0, 0, 0, 0.6);
}
.modal-nav-button.prev { left: 20px; }
.modal-nav-button.next { right: 20px; }

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity 0.3s ease;
}
.modal-fade-enter-from,
.modal-fade-leave-to {
  opacity: 0;
}
.modal-fade-enter-active .modal-content,
.modal-fade-leave-active .modal-content {
  transition: transform 0.3s ease;
}
.modal-fade-enter-from .modal-content,
.modal-fade-leave-to .modal-content {
  transform: scale(0.9);
}

/* Fireworks Styles */
.fireworks-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9999;
}
.particle {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  animation: firework-explode 1s ease-out forwards;
}
@keyframes firework-explode {
  0% { transform: translate(-50%, -50%) scale(1); opacity: 1; }
  100% { transform: translate(calc(var(--x) - 50%), calc(var(--y) - 50%)) scale(0); opacity: 0; }
}

@media (max-width: 768px) {
  .modal-content {
    max-width: 95vw;
    max-height: 85vh;
  }
  .hero-section {
    min-height: 80vh;
    padding: 1rem;
  }
  .hero-text .title {
    font-size: 3.2rem;
    text-shadow: 3px 3px 0px #ff9a8b, 6px 6px 0px rgba(0,0,0,0.1);
  }
  .hero-text .subtitle {
    font-size: 1.2rem;
  }
  .hamo-character {
    width: 110px;
  }
  .hero-image {
    width: 80%;
    transform: rotate(-3deg);
  }
  .section {
    padding: 2rem 1rem;
    margin: 0 1rem 2rem 1rem;
  }
  .section-title {
    font-size: 1.8rem;
  }
  .intro-text {
    font-size: 1.1rem;
  }
  .photo-gallery {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
  .guestbook-link {
    font-size: 1.2rem;
    padding: 0.8rem 2rem;
  }
  .modal-nav-button {
    width: 45px;
    height: 45px;
    font-size: 2rem;
  }
  .modal-nav-button.prev { left: 10px; }
  .modal-nav-button.next { right: 10px; }
}
</style>
