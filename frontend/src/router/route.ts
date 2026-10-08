import { RouteRecordRaw } from 'vue-router';

/**
 * 建议：路由 path 路径与文件夹名称相同，找文件可浏览器地址找，方便定位文件位置
 *
 * 路由meta对象参数说明
 * meta: {
 *      title:          菜单栏及 tagsView 栏、菜单搜索名称（国际化）
 *      isLink：        是否超链接菜单，开启外链条件，`1、isLink: 链接地址不为空 2、isIframe:false`
 *      isHide：        是否隐藏此路由
 *      isKeepAlive：   是否缓存组件状态
 *      isAffix：       是否固定在 tagsView 栏上
 *      isIframe：      是否内嵌窗口，开启条件，`1、isIframe:true 2、isLink：链接地址不为空`
 *      roles：         当前路由权限标识，取角色管理。控制路由显示、隐藏。超级管理员：admin 普通角色：common
 *      icon：          菜单、tagsView 图标，阿里：加 `iconfont xxx`，fontawesome：加 `fa xxx`
 * }
 */

// 扩展 RouteMeta 接口
declare module 'vue-router' {
	interface RouteMeta {
		title?: string;
		isLink?: string;
		isHide?: boolean;
		isKeepAlive?: boolean;
		isAffix?: boolean;
		isIframe?: boolean;
		roles?: string[];
		icon?: string;
	}
}

/**
 * 定义动态路由
 * 前端添加路由，请在顶级节点的 `children 数组` 里添加
 * @description 未开启 isRequestRoutes 为 true 时使用（前端控制路由），开启时第一个顶级 children 的路由将被替换成接口请求回来的路由数据
 * @description 各字段请查看 `/@/views/system/menu/component/addMenu.vue 下的 ruleForm`
 * @returns 返回路由菜单数据
 */
export const dynamicRoutes: Array<RouteRecordRaw> = [
	{
		path: '/',
		name: '/',
		component: () => import('/@/layout/index.vue'),
		redirect: '/homePage',
		meta: {
			isKeepAlive: true,
		},
		children: [
			{
				path: '/homePage',
				name: 'homePage',
				component: () => import('/@/views/homePage/index.vue'),
				meta: {
					title: '首页',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-sy',
				},
			},
			{
				path: '/dataView',
				name: 'dataView',
				component: () => import('/@/views/dataView/index.vue'),
				meta: {
					title: '数据大屏',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-sj',
				},
			},
			{
				path: '/infoGreenhouse',
				name: 'infoGreenhouse',
				component: () => import('/@/views/infoGreenhouse/index.vue'),
				meta: {
					title: '智能温室',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-znws',
				},
			},
			{
				path: '/detailsEnv',
				name: 'detailsEnv',
				component: () => import('/@/views/detailsEnv/index.vue'),
				meta: {
					title: '环境监测',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-huanjingjiance',
				},
			},
			{
				path: '/diseaseDetection',
				name: 'diseaseDetection',
				component: () => import('/@/layout/routerView/parent.vue'),
				redirect: '/infoDisease',
				meta: {
					title: '病虫害检测',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-bingchonghai-1haichong',
				},
				children: [
					{
						path: '/infoDisease',
						name: 'infoDisease',
						component: () => import('/@/views/infoDisease/index.vue'),
						meta: {
							title: '病虫害数据库',
							isLink: '',
							isHide: false,
							isKeepAlive: true,
							isAffix: false,
							isIframe: false,
							roles: ['admin', 'common', 'others'],
							icon: 'iconfontjs icon-bingchonghai-1haichong',
						},
					},
					{
						path: '/imgPredict',
						name: 'imgPredict',
						component: () => import('/@/views/imgPredict/index.vue'),
						meta: {
							title: '图像检测',
							isLink: '',
							isHide: false,
							isKeepAlive: true,
							isAffix: false,
							isIframe: false,
							roles: ['admin', 'common', 'others'],
							icon: 'iconfontjs icon-tpjc',
						},
					},
					{
						path: '/videoPredict',
						name: 'videoPredict',
						component: () => import('/@/views/videoPredict/index.vue'),
						meta: {
							title: '视频检测',
							isLink: '',
							isHide: false,
							isKeepAlive: true,
							isAffix: false,
							isIframe: false,
							roles: ['admin', 'common', 'others'],
							icon: 'iconfontjs icon-spjc',
						},
					},
					{
						path: '/cameraPredict',
						name: 'cameraPredict',
						component: () => import('/@/views/cameraPredict/index.vue'),
						meta: {
							title: '摄像检测',
							isLink: '',
							isHide: false,
							isKeepAlive: true,
							isAffix: false,
							isIframe: false,
							roles: ['admin', 'common', 'others'],
							icon: 'iconfontjs icon-shexiangtoujiance',
						},
					},
					{
						path: '/imgRecord',
						name: 'imgRecord',
						component: () => import('/@/views/imgRecord/index.vue'),
						meta: {
							title: '图片识别记录',
							isLink: '',
							isHide: false,
							isKeepAlive: true,
							isAffix: false,
							isIframe: false,
							roles: ['admin', 'common', 'others'],
							icon: 'iconfontjs icon-tpjl',
						},
					},
					{
						path: '/videoRecord',
						name: 'videoRecord',
						component: () => import('/@/views/videoRecord/index.vue'),
						meta: {
							title: '视频识别记录',
							isLink: '',
							isHide: false,
							isKeepAlive: true,
							isAffix: false,
							isIframe: false,
							roles: ['admin', 'common', 'others'],
							icon: 'iconfontjs icon-spjl',
						},
					},
					{
						path: '/cameraRecord',
						name: 'cameraRecord',
						component: () => import('/@/views/cameraRecord/index.vue'),
						meta: {
							title: '摄像识别记录',
							isLink: '',
							isHide: false,
							isKeepAlive: true,
							isAffix: false,
							isIframe: false,
							roles: ['admin', 'common', 'others'],
							icon: 'iconfontjs icon-sxtjl',
						},
					}
				]
			},
			{
				path: '/purchaseManage',
				name: 'purchaseManage',
				component: () => import('/@/views/purchaseManage/index.vue'),
				meta: {
					title: '农资采购',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-gr',
				},
			},
			{
				path: '/storageManage',
				name: 'storageManage',
				component: () => import('/@/views/storageManage/index.vue'),
				meta: {
					title: '库存管理',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-cc',
				},
			},
			{
				path: '/smartChat',
				name: 'smartChat',
				component: () => import('/@/views/smartChat/index.vue'),
				meta: {
					title: '智能助手',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-znwd',
				},
			},
			{
				path: '/usermanage',
				name: 'usermanage',
				component: () => import('/@/views/userManage/index.vue'),
				meta: {
					title: '用户管理',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin'],
					icon: 'iconfontjs icon-yh',
				},
			},
			{
				path: '/personal',
				name: 'personal',
				component: () => import('/@/views/personal/index.vue'),
				meta: {
					title: '个人中心',
					isLink: '',
					isHide: false,
					isKeepAlive: true,
					isAffix: false,
					isIframe: false,
					roles: ['admin', 'common', 'others'],
					icon: 'iconfontjs icon-grzx',
				},
			},
		],
	},
];

/**
 * 定义404、401界面
 * @link 参考：https://next.router.vuejs.org/zh/guide/essentials/history-mode.html#netlify
 */
export const notFoundAndNoPower = [
	{
		path: '/:path(.*)*',
		name: 'notFound',
		component: () => import('/@/views/error/404.vue'),
		meta: {
			title: 'message.staticRoutes.notFound',
			isHide: true,
		},
	},
	{
		path: '/401',
		name: 'noPower',
		component: () => import('/@/views/error/401.vue'),
		meta: {
			title: 'message.staticRoutes.noPower',
			isHide: true,
		},
	},
];

/**
 * 定义静态路由（默认路由）
 * 此路由不要动，前端添加路由的话，请在 `dynamicRoutes 数组` 中添加
 * @description 前端控制直接改 dynamicRoutes 中的路由，后端控制不需要修改，请求接口路由数据时，会覆盖 dynamicRoutes 第一个顶级 children 的内容（全屏，不包含 layout 中的路由出口）
 * @returns 返回路由菜单数据
 */
export const staticRoutes: Array<RouteRecordRaw> = [
	{
		path: '/login',
		name: 'login',
		component: () => import('/@/views/login/index.vue'),
		meta: {
			title: '登录',
		},
	},
	{
		path: '/register',
		name: 'register',
		component: () => import('/@/views/login/register.vue'),
		meta: {
			title: '注册',
		},
	},
	{
		path: '/videoShow',
		name: 'videoShow',
		component: () => import('/@/views/videoRecord/show.vue'),
		meta: {
			title: '记录查看',
		},
	},
	/**
	 * 提示：写在这里的为全屏界面，不建议写在这里
	 * 请写在 `dynamicRoutes` 路由数组中
	 */
];
