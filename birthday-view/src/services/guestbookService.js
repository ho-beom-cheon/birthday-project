import axios from 'axios';
const API_BASE = import.meta.env.VITE_API_BASE;
const API_URL = `${API_BASE}/messages`;

class GuestbookService {
  /**
   * 모든 방명록 메시지를 가져옵니다.
   * GET /api/messages
   */
  getMessages() {
    return axios.get(API_URL);
  }

  /**
   * 새로운 방명록 메시지를 생성합니다.
   * POST /api/messages
   * @param {{ author: string, message: string }} message - 생성할 메시지 데이터
   */
  createMessage(message) {
    return axios.post(API_URL, message, {
      headers: { 'Content-Type': 'application/json' }
    });
  }

  /**
   * 방명록 메시지에 좋아요를 추가합니다.
   * POST /api/messages/{id}/like
   * @param {number} id - 좋아요를 누를 메시지의 ID
   */
  likeMessage(id) {
    return axios.post(`${API_URL}/${id}/like`);
  }
}

export default new GuestbookService();