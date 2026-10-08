<template>
	<div class="system-role-container layout-padding">
		<div class="system-role-padding layout-padding-auto layout-padding-view">
			<div class="system-user-search mb15">
				<el-input v-model="state.tableData.param.name" size="default" placeholder="请输入病害名称" clearable style="width: 180px"> </el-input>
				<el-select v-model="state.tableData.param.cropType" size="default" class="ml10" placeholder="请选择作物类型" clearable style="width: 180px">
					<el-option v-for="item in state.cropTypes" :key="item" :label="item" :value="item"></el-option>
				</el-select>
				<el-button size="default" type="primary" class="ml10" @click="getTableData()">
					<el-icon>
						<ele-Search />
					</el-icon>
					查询
				</el-button>
				<el-button size="default" type="success" class="ml10" @click="onOpenAddDisease('add')">
					<el-icon>
						<ele-FolderAdd />
					</el-icon>
					添加
				</el-button>
			</div>
			<el-table :data="state.tableData.data" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column prop="num" label="序号" width="80" align="center" />
				<el-table-column prop="cropType" label="作物类型" show-overflow-tooltip width="80" align="center" />
				<el-table-column prop="name" label="病害名称" show-overflow-tooltip width="150" align="center" />
				<el-table-column prop="symptoms" label="病害症状" show-overflow-tooltip min-width="200" align="center"/>
				<el-table-column prop="causes" label="发病原因" show-overflow-tooltip min-width="200" align="center"/>
				<el-table-column prop="prevention" label="防治方法" show-overflow-tooltip min-width="200" align="center"/>
				<el-table-column label="图片" width="100" align="center">
					<template #default="scope">
						<el-image 
							style="width: 35px; height: 35px" 
							:src="scope.row.image" 
							:preview-src-list="[scope.row.image]"
							:preview-teleported="true"
							:hide-on-click-modal="true"
							fit="cover"
							:preview-options="{
								zoom: false,
								closeOnPressEscape: true,
								toolbar: false
							}"
						/>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="150" fixed="right" align="center">
					<template #default="scope">
						<el-button size="small" text type="primary" @click="onOpenDetail(scope.row)">详情</el-button>
						<el-button size="small" text type="primary" @click="onOpenEditDisease('edit', scope.row)">修改</el-button>
						<el-button size="small" text type="primary" @click="onRowDel(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				@size-change="onHandleSizeChange"
				@current-change="onHandleCurrentChange"
				class="mt15"
				:pager-count="5"
				:page-sizes="[10, 20, 30]"
				v-model:current-page="state.tableData.param.pageNum"
				background
				v-model:page-size="state.tableData.param.pageSize"
				layout="total, sizes, prev, pager, next, jumper"
				:total="state.tableData.total"
			>
			</el-pagination>
		</div>
		<DiseaseDialog ref="diseaseDialogRef" @refresh="getTableData()" />
		<DiseaseDetail ref="diseaseDetailRef" />
	</div>
</template>

<script setup lang="ts" name="systemRole">
import { defineAsyncComponent, reactive, onMounted, ref } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import request from '/@/utils/request';

// 引入组件
const DiseaseDialog = defineAsyncComponent(() => import('./dialog.vue'));
const DiseaseDetail = defineAsyncComponent(() => import('./detail.vue'));

// 定义变量内容
const diseaseDialogRef = ref();
const diseaseDetailRef = ref();
const state = reactive({
	tableData: {
		data: [] as any,
		total: 0,
		loading: false,
		param: {
			name: '',
			cropType: '',
			pageNum: 1,
			pageSize: 10,
		},
	},
	cropTypes: ['玉米', '水稻', '小麦', '马铃薯', '棉花', '苹果', '葡萄', '番茄', '草莓'] // 作物类型选项
});

// 获取表格数据
const getTableData = () => {
	state.tableData.loading = true;
	request
		.get('/api/disease', {
			params: state.tableData.param,
		})
		.then((res) => {
			if (res.code == 0) {
				state.tableData.data = [];
				setTimeout(() => {
					state.tableData.loading = false;
				}, 500);
				for (let i = 0; i < res.data.records.length; i++) {
					state.tableData.data[i] = res.data.records[i];
					state.tableData.data[i]['num'] = i + 1;
				}
				state.tableData.total = res.data.total;
			} else {
				ElMessage({
					type: 'error',
					message: res.msg,
				});
			}
		});
};

// 打开新增病害弹窗
const onOpenAddDisease = (type: string) => {
	diseaseDialogRef.value.openDialog(type);
};

// 打开修改病害弹窗
const onOpenEditDisease = (type: string, row: Object) => {
	diseaseDialogRef.value.openDialog(type, row);
};

// 打开详情弹窗
const onOpenDetail = (row: any) => {
	diseaseDetailRef.value.openDialog(row);
};

// 删除病害
const onRowDel = (row: any) => {
	ElMessageBox.confirm(`此操作将永久删除该病害信息，是否继续?`, '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => {
			request.delete('/api/disease/' + row.id).then((res) => {
				if (res.code == 0) {
					ElMessage({
						type: 'success',
						message: '删除成功！',
					});
					setTimeout(() => {
						getTableData();
					}, 500);
				} else {
					ElMessage({
						type: 'error',
						message: res.msg,
					});
				}
			});
		})
		.catch(() => {});
};

// 分页改变
const onHandleSizeChange = (val: number) => {
	state.tableData.param.pageSize = val;
	getTableData();
};

// 分页改变
const onHandleCurrentChange = (val: number) => {
	state.tableData.param.pageNum = val;
	getTableData();
};

// 页面加载时
onMounted(() => {
	getTableData();
});
</script>

<style scoped lang="scss">
.system-role-container {
	.system-role-padding {
		padding: 15px;
		.el-table {
			flex: 1;
			:deep(.el-table__row) {
				height: 40px;  // 设置行高
			}
			:deep(.el-table__header) {
				th {
					padding: 6px 0;  // 减小表头padding
				}
			}
			:deep(.el-table__cell) {
				padding: 3px 0;  // 减小单元格padding
			}
			:deep(.cell) {
				line-height: 1.3;  // 减小文字行高
			}
		}
		:deep(.el-input) {
			height: 32px;
			line-height: 32px;
		}
		:deep(.el-select) {
			height: 32px;
			line-height: 32px;
			.el-input {
				height: 32px;
				line-height: 32px;
			}
		}
	}
}
</style> 