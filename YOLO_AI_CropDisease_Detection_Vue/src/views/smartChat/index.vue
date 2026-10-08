<template>
	<div class="chat-container">
	  <div class="chat-header">
		<h3 class="chat-title">智能助手 DeepSeek Chat</h3>
	  </div>
	  
	  <div class="chat-messages" ref="messageContainer">
		<div v-for="(message, index) in messages" :key="index" 
			 :class="['message', message.role === 'user' ? 'user-message' : 'assistant-message']">
		  <div class="message-content">
			<div class="avatar">
			  {{ message.role === 'user' ? '👤' : '🤖' }}
			</div>
			<div class="text">{{ message.content }}</div>
		  </div>
		</div>
		<div v-if="loading" class="message assistant-message">
		  <div class="message-content">
			<div class="avatar">🤖</div>
			<div class="text">
			  <span class="loading-dots">思考中</span>
			</div>
		  </div>
		</div>
	  </div>
  
	  <div class="suggested-questions" v-if="messages.length === 1">
		<div class="suggested-title" style="font-size: 1.2rem; font-weight: 550;">猜你想问</div>
		<div class="suggested-list">
		  <div v-for="(question, index) in suggestedQuestions" 
			   :key="index" 
			   class="suggested-item"
			   @click="selectQuestion(question)">
			{{ question }}
		  </div>
		</div>
	  </div>

	  <div class="chat-input">
		<el-input
		  v-model="userInput"
		  type="textarea"
		  :rows="3"
		  placeholder="请输入您的问题..."
		  @keyup.enter.ctrl="sendMessage"
		/>
		<el-button 
		  type="primary" 
		  :loading="loading"
		  @click="sendMessage"
		  :disabled="!userInput.trim()"
		>
		  发送
		</el-button>
	  </div>
	</div>
  </template>
  
  <script>
  import axios from 'axios'
  
  export default {
	name: 'SmartChat',
	data() {
	  return {
		messages: [{
		  role: 'assistant',
		  content: '你好，我是你的农业智能小助手，你可以向我提问！'
		}],
		userInput: '',
		loading: false,
		suggestedQuestions: [
		  '如何防治水稻病害？',
		  '玉米常见病虫害有哪些？',
		  '小麦生长周期是多少天？',
		  '如何提高农作物产量？',
		  '农药使用注意事项有哪些？',
		  '农作物施肥的最佳时间？',
		  '如何识别植物病害症状？'
		]
	  }
	},
	methods: {
	  selectQuestion(question) {
		this.userInput = question
		this.sendMessage()
	  },
	  async sendMessage() {
		if (!this.userInput.trim() || this.loading) return
  
		const userMessage = this.userInput.trim()
		this.messages.push({
		  role: 'user',
		  content: userMessage
		})
		
		this.userInput = ''
		this.loading = true
  
		try {
		  const response = await axios.post('/api/ai/chat', {
			model: 'deepseek-chat',
			messages: [
			  ...this.messages.map(msg => ({
				role: msg.role,
				content: msg.content
			  }))
			],
			stream: false
		  })
  
		  this.messages.push({
			role: 'assistant',
			content: response.data.choices[0].message.content
		  })
		} catch (error) {
		  this.$message.error('发送消息失败，请稍后重试')
		  console.error('Error:', error)
		} finally {
		  this.loading = false
		  this.$nextTick(() => {
			this.scrollToBottom()
		  })
		}
	  },
	  scrollToBottom() {
		const container = this.$refs.messageContainer
		container.scrollTop = container.scrollHeight
	  }
	}
  }
  </script>
  
  <style scoped>
  .chat-container {
	height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
	background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
  }
  
  .chat-header {
	padding: 20px;
	background: rgba(255, 255, 255, 0.9);
	backdrop-filter: blur(10px);
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
	border-bottom: 1px solid rgba(0, 0, 0, 0.05);
	display: flex;
	justify-content: center;
	align-items: center;
  }
  
  .chat-title {
	margin: 0;
	color: #2c3e50;
	font-size: 1.5rem;
	font-weight: 550;
	text-align: center;
  }
  
  .chat-messages {
	flex: 1;
	overflow-y: auto;
	padding: 20px;
	scroll-behavior: smooth;
	width: 100%;
  }
  
  .message {
	margin-bottom: 24px;
	opacity: 0;
	transform: translateY(20px);
	animation: messageAppear 0.3s ease forwards;
	display: flex;
	justify-content: flex-start;
	width: 100%;
  }
  
  @keyframes messageAppear {
	to {
	  opacity: 1;
	  transform: translateY(0);
	}
  }
  
  .message-content {
	display: flex;
	align-items: flex-start;
	max-width: 70%;
	gap: 12px;
	word-break: break-word;
  }
  
  .user-message .message-content {
	flex-direction: row-reverse;
	margin-left: auto;
	padding-left: 10%;
  }
  
  .assistant-message .message-content {
	margin-right: auto;
	padding-right: 10%;
  }
  
  .avatar {
	width: 36px;
	height: 36px;
	min-width: 36px;
	flex-shrink: 0;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	background: rgba(255, 255, 255, 0.9);
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	font-size: 1.1rem;
	transition: transform 0.2s ease;
  }
  
  .user-message .avatar {
	margin-right: 8px;
  }
  
  .assistant-message .avatar {
	margin-left: 8px;
  }
  
  .text {
	padding: 12px 16px;
	border-radius: 12px;
	background: rgba(255, 255, 255, 0.9);
	box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
	line-height: 1.6;
	font-size: 0.95rem;
	position: relative;
	transition: all 0.3s ease;
	flex: 1;
	min-width: 0;
	white-space: normal;
	word-wrap: break-word;
  }
  
  .user-message .text {
	background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
	color: #fff;
	border-top-right-radius: 4px;
  }
  
  .assistant-message .text {
	border-top-left-radius: 4px;
  }
  
  .chat-input {
	padding: 20px;
	background: rgba(255, 255, 255, 0.9);
	backdrop-filter: blur(10px);
	box-shadow: 0 -4px 6px rgba(0, 0, 0, 0.05);
	display: flex;
	gap: 12px;
  }
  
  .chat-input :deep(.el-textarea__inner) {
	border-radius: 12px;
	border: 1px solid rgba(0, 0, 0, 0.1);
	padding: 12px;
	font-size: 0.95rem;
	resize: none;
	transition: all 0.3s ease;
  }
  
  .chat-input :deep(.el-textarea__inner:focus) {
	border-color: #4CAF50;
	box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.1);
  }
  
  .chat-input :deep(.el-button) {
	border-radius: 12px;
	padding: 0 24px;
	height: auto;
	background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
	border: none;
	transition: all 0.3s ease;
  }
  
  .chat-input :deep(.el-button:hover) {
	transform: translateY(-1px);
	box-shadow: 0 4px 12px rgba(76, 175, 80, 0.2);
  }
  
  .chat-input :deep(.el-button:disabled) {
	background: #ccc;
	transform: none;
	box-shadow: none;
  }
  
  .loading-dots {
	display: inline-block;
	position: relative;
	color: #666;
  }
  
  .loading-dots::after {
	content: '...';
	animation: loading 1.5s infinite;
  }
  
  @keyframes loading {
	0% { content: '.'; }
	33% { content: '..'; }
	66% { content: '...'; }
  }
  
  /* 自定义滚动条样式 */
  .chat-messages::-webkit-scrollbar {
	width: 6px;
  }
  
  .chat-messages::-webkit-scrollbar-track {
	background: rgba(0, 0, 0, 0.05);
	border-radius: 3px;
  }
  
  .chat-messages::-webkit-scrollbar-thumb {
	background: rgba(0, 0, 0, 0.2);
	border-radius: 3px;
  }
  
  .chat-messages::-webkit-scrollbar-thumb:hover {
	background: rgba(0, 0, 0, 0.3);
  }
  
  .suggested-questions {
	padding: 20px;
	background: rgba(255, 255, 255, 0.9);
	backdrop-filter: blur(10px);
	border-top: 1px solid rgba(0, 0, 0, 0.05);
  }
  
  .suggested-title {
	font-size: 0.9rem;
	color: #666;
	margin-bottom: 12px;
	font-weight: 500;
  }
  
  .suggested-list {
	display: flex;
	flex-wrap: wrap;
	gap: 8px;
  }
  
  .suggested-item {
	padding: 8px 16px;
	background: rgba(76, 175, 80, 0.1);
	border-radius: 20px;
	font-size: 0.9rem;
	color: #4CAF50;
	cursor: pointer;
	transition: all 0.3s ease;
  }
  
  .suggested-item:hover {
	background: rgba(76, 175, 80, 0.2);
	transform: translateY(-1px);
  }
  </style>
  