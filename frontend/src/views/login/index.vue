<template>
	<div class="login-container">
		<div class="bg-bubbles">
			<li v-for="n in 10" :key="n"></li>
		</div>

		<div class="login-box animate__animated animate__fadeIn">
			<div class="title">
				<h2>基于YOLO+AI的农作物病虫害检测与环境监测一体化智能云平台</h2>
			</div>

			<el-form :model="ruleForm" :rules="registerRules" ref="ruleFormRef">
				<el-form-item prop="username">
					<el-input v-model="ruleForm.username" placeholder="请输入用户名" prefix-icon="User" class="custom-input" />
				</el-form-item>

				<el-form-item prop="password">
					<el-input v-model="ruleForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password class="custom-input" />
				</el-form-item>

				<el-form-item>
					<el-button type="primary" class="login-btn" @click="submitForm(ruleFormRef)"> 登录 </el-button>
				</el-form-item>
			</el-form>

			<div class="options">
				<router-link to="/register">注册账号</router-link>
				<span>|</span>
				<a href="#">忘记密码</a>
			</div>
		</div>
	</div>
</template>

<script lang="ts" setup>
import { reactive, computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useI18n } from 'vue-i18n';
import Cookies from 'js-cookie';
import { storeToRefs } from 'pinia';
import { useThemeConfig } from '/@/stores/themeConfig';
import { initFrontEndControlRoutes } from '/@/router/frontEnd';
import { initBackEndControlRoutes } from '/@/router/backEnd';
import { Session } from '/@/utils/storage';
import { formatAxis } from '/@/utils/formatTime';
import { NextLoading } from '/@/utils/loading';
import type { FormInstance, FormRules } from 'element-plus';
import request from '/@/utils/request';

// 定义变量内容
const { t } = useI18n();
const storesThemeConfig = useThemeConfig();
const { themeConfig } = storeToRefs(storesThemeConfig);
const route = useRoute();
const router = useRouter();
const formSize = ref('default');
const ruleFormRef = ref<FormInstance>();

/*
 * 定义全局变量，等价Vue2中的data() return。
 */
const ruleForm = reactive({
	username: '',
	password: '',
});

/*
 * 校验规则。
 */
const registerRules = reactive<FormRules>({
	username: [
		{ required: true, message: '请输入账号', trigger: 'blur' },
		{ min: 3, max: 20, message: '长度在3-20个字符', trigger: 'blur' },
	],
	password: [
		{ required: true, message: '请输入密码', trigger: 'blur' },
		{ min: 3, max: 20, message: '长度在3-20个字符', trigger: 'blur' },
	],
});

/*
 * 提交后的方法。
 */
// 时间获取
const currentTime = computed(() => {
	return formatAxis(new Date());
});
// 登录
const onSignIn = async () => {
	// 存储 token 到浏览器缓存
	Session.set('token', Math.random().toString(36).substr(0));
	// 模拟数据，对接接口时，记得删除多余代码及对应依赖的引入。用于 `/src/stores/userInfo.ts` 中不同用户登录判断（模拟数据）
	Cookies.set('userName', ruleForm.username);
	if (!themeConfig.value.isRequestRoutes) {
		// 前端控制路由，2、请注意执行顺序
		const isNoPower = await initFrontEndControlRoutes();
		signInSuccess(isNoPower);
	} else {
		// 模拟后端控制路由，isRequestRoutes 为 true，则开启后端控制路由
		// 添加完动态路由，再进行 router 跳转，否则可能报错 No match found for location with path "/"
		const isNoPower = await initBackEndControlRoutes();
		// 执行完 initBackEndControlRoutes，再执行 signInSuccess
		signInSuccess(isNoPower);
	}
};
// 登录成功后的跳转
const signInSuccess = (isNoPower: boolean | undefined) => {
	if (isNoPower) {
		ElMessage.warning('抱歉，您没有登录权限');
		Session.clear();
	} else {
		// 初始化登录成功时间问候语
		let currentTimeInfo = currentTime.value;
		// 登录成功，跳到转首页
		// 如果是复制粘贴的路径，非首页/登录页，那么登录成功后重定向到对应的路径中
		if (route.query?.redirect) {
			router.push({
				path: <string>route.query?.redirect,
				query: Object.keys(<string>route.query?.params).length > 0 ? JSON.parse(<string>route.query?.params) : '',
			});
		} else {
			router.push('/');
		}
		// 登录成功提示
		const signInText = t('message.signInText');
		ElMessage.success(`${currentTimeInfo}，${signInText}`);
		// 添加 loading，防止第一次进入界面时出现短暂空白
		NextLoading.start();
	}
};
const submitForm = (formEl: FormInstance | undefined) => {
	if (!formEl) return;
	formEl.validate((valid) => {
		if (valid) {
			request.post('/api/user/login', ruleForm).then((res) => {
				console.log(res);
				if (res.code == 0) {
					Cookies.set('role', res.data.role); //  设置角色
					//登录成功
					onSignIn();
				} else {
					ElMessage({
						type: 'error',
						message: res.msg,
					});
				}
			});
		} else {
			console.log('error submit!');
			return false;
		}
	});
};
</script>

<style scoped>
.login-container {
	min-height: 100vh;
	width: 100vw;
	display: flex;
	align-items: center;
	justify-content: center;
	background-image: url('/bg1.png');
	background-position: center;
	background-repeat: no-repeat;
	background-size: 100% 100%;
	padding: 20px;
}

.login-box {
	position: relative;
	z-index: 2;
	transform: translateY(20px);
	animation: slideUp 0.8s forwards;
	opacity: 0;
	width: 460px;
	padding: 40px 50px;
	background: rgba(255, 255, 255, 0.95);
	border-radius: 16px;
	box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
	backdrop-filter: blur(10px);
}

.title {
	text-align: center;
	margin-bottom: 35px;
}

.title h2 {
	font-size: 20px;
	color: #2c3e50;
	margin-bottom: 10px;
	font-weight: 600;
}

.title p {
	font-size: 10px;
	color: #7f8c8d;
	letter-spacing: 1px;
}

:deep(.custom-input .el-input__wrapper) {
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
	border-radius: 8px;
	padding: 12px 15px;
	background: #f8fafc;
}

:deep(.custom-input .el-input__wrapper:hover) {
	box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

:deep(.custom-input .el-input__wrapper.is-focus) {
	box-shadow: 0 0 0 1px #409eff;
	background: #fff;
}

.login-btn {
	width: 100%;
	padding: 12px 0;
	font-size: 16px;
	font-weight: 500;
	letter-spacing: 1px;
	border-radius: 8px;
	background: linear-gradient(to right, #2f80ed 0%, #56ccf2 100%);
	border: none;
	margin-top: 10px;
	transition: transform 0.3s ease;
}

.login-btn:hover {
	transform: translateY(-2px);
	background: linear-gradient(to right, #2f80ed 0%, #56ccf2 100%);
	opacity: 0.9;
}

.options {
	margin-top: 25px;
	text-align: center;
}

.options a {
	color: #2f80ed;
	text-decoration: none;
	font-size: 15px;
	transition: all 0.3s ease;
	font-weight: 500;
}

.options span {
	color: #ddd;
	margin: 0 15px;
}

.options a:hover {
	color: #56ccf2;
	text-decoration: underline;
}

/* 响应式适配 */
@media (max-width: 768px) {
	.login-box {
		width: 90%;
		padding: 30px 20px;
	}

	.title h2 {
		font-size: 24px;
	}

	.title p {
		font-size: 14px;
	}
}

/* 添加背景气泡动画 */
.bg-bubbles {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: 1;
	overflow: hidden;
}

.bg-bubbles li {
	position: absolute;
	list-style: none;
	display: block;
	width: 40px;
	height: 40px;
	background-color: rgba(255, 255, 255, 0.15);
	bottom: -160px;
	animation: square 25s infinite;
	transition-timing-function: linear;
}

.bg-bubbles li:nth-child(1) {
	left: 10%;
	width: 80px;
	height: 80px;
	animation-delay: 0s;
}

.bg-bubbles li:nth-child(2) {
	left: 20%;
	width: 90px;
	height: 90px;
	animation-delay: 2s;
	animation-duration: 17s;
}

.bg-bubbles li:nth-child(3) {
	left: 25%;
	animation-delay: 4s;
}

.bg-bubbles li:nth-child(4) {
	left: 40%;
	width: 60px;
	height: 60px;
	animation-duration: 22s;
}

.bg-bubbles li:nth-child(5) {
	left: 70%;
	width: 120px;
	height: 120px;
}

.bg-bubbles li:nth-child(6) {
	left: 80%;
	width: 90px;
	height: 90px;
	animation-delay: 3s;
}

.bg-bubbles li:nth-child(7) {
	left: 32%;
	width: 60px;
	height: 60px;
	animation-delay: 7s;
}

.bg-bubbles li:nth-child(8) {
	left: 55%;
	width: 20px;
	height: 20px;
	animation-delay: 15s;
	animation-duration: 40s;
}

.bg-bubbles li:nth-child(9) {
	left: 25%;
	width: 10px;
	height: 10px;
	animation-delay: 2s;
	animation-duration: 40s;
}

.bg-bubbles li:nth-child(10) {
	left: 90%;
	width: 160px;
	height: 160px;
	animation-delay: 11s;
}

@keyframes square {
	0% {
		transform: translateY(0) rotate(0deg);
		opacity: 1;
	}
	100% {
		transform: translateY(-1000px) rotate(600deg);
		opacity: 0;
	}
}

/* 修改登录框样式 */
.login-box {
	position: relative;
	z-index: 2;
	transform: translateY(20px);
	animation: slideUp 0.8s forwards;
	opacity: 0;
	width: 460px;
	padding: 40px 50px;
	background: rgba(255, 255, 255, 0.95);
	border-radius: 16px;
	box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
	backdrop-filter: blur(10px);
}

@keyframes slideUp {
	from {
		transform: translateY(20px);
		opacity: 0;
	}
	to {
		transform: translateY(0);
		opacity: 1;
	}
}

/* 修改输入框动画部分 */
:deep(.el-form-item) {
	opacity: 0;
}

:deep(.el-form-item:nth-child(odd)) {
	transform: translateX(-50px);
	animation: slideRightIn 0.5s forwards;
}

:deep(.el-form-item:nth-child(even)) {
	transform: translateX(50px);
	animation: slideLeftIn 0.5s forwards;
}

:deep(.el-form-item:nth-child(1)) {
	animation-delay: 0.2s;
}
:deep(.el-form-item:nth-child(2)) {
	animation-delay: 0.4s;
}

@keyframes slideRightIn {
	from {
		transform: translateX(-50px);
		opacity: 0;
	}
	to {
		transform: translateX(0);
		opacity: 1;
	}
}

@keyframes slideLeftIn {
	from {
		transform: translateX(50px);
		opacity: 0;
	}
	to {
		transform: translateX(0);
		opacity: 1;
	}
}

/* 按钮悬浮效果增强 */
.login-btn {
	transition: all 0.3s ease;
}

.login-btn:hover {
	transform: translateY(-3px);
	box-shadow: 0 7px 14px rgba(50, 50, 93, 0.1), 0 3px 6px rgba(0, 0, 0, 0.08);
}

.login-btn:active {
	transform: translateY(-1px);
}

/* 输入框焦点动画 */
:deep(.el-input__wrapper.is-focus) {
	animation: pulse 0.3s ease-in-out;
}

@keyframes pulse {
	0% {
		transform: scale(1);
	}
	50% {
		transform: scale(1.02);
	}
	100% {
		transform: scale(1);
	}
}
</style>
