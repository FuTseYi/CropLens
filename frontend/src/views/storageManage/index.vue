<template>
	<div class="system-role-container layout-padding">
		<div class="system-role-padding layout-padding-auto layout-padding-view">
			<div class="system-user-search mb15">
				<el-input v-model="state.tableData.param.search" size="default" placeholder="请输入产品名称" style="max-width: 180px"> </el-input>
				<el-input v-model="state.tableData.param.warehouse" size="default" placeholder="请输入仓库" class="ml10" style="max-width: 180px"> </el-input>
				<el-input v-model="state.tableData.param.storageArea" size="default" placeholder="请输入存储区" class="ml10" style="max-width: 180px"> </el-input>
				<el-input v-model="state.tableData.param.manager" size="default" placeholder="请输入仓库管理员" class="ml10" style="max-width: 180px"> </el-input>
				<!-- <el-input v-model="state.tableData.param.phone" size="default" placeholder="请输入手机号" class="ml10" style="max-width: 180px"> </el-input> -->
				<el-button size="default" type="primary" class="ml10" @click="getTableData()">
					<el-icon>
						<ele-Search />
					</el-icon>
					查询
				</el-button>
				<el-button size="default" type="success" class="ml10" @click="onOpenAddStorage('add')">
					<el-icon>
						<ele-FolderAdd />
					</el-icon>
					添加
				</el-button>
			</div>
			<el-table :data="state.tableData.data" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column prop="id" label="ID" width="80" align="center" />
				<el-table-column prop="product" label="产品" show-overflow-tooltip width="125" align="center" />
				<el-table-column prop="warehouse" label="用途" show-overflow-tooltip width="180" align="center" />
				<el-table-column prop="storageArea" label="存储区" show-overflow-tooltip width="125" align="center" />
				<el-table-column prop="quantity" label="数量" width="125" align="center" />
				<el-table-column prop="manager" label="仓库管理员" show-overflow-tooltip width="125" align="center" />
				<el-table-column prop="phone" label="手机号" show-overflow-tooltip width="150" align="center" />
				<el-table-column prop="remark" label="备注" show-overflow-tooltip width="180" align="center" />
				<el-table-column label="操作" width="150" fixed="right" align="center">
					<template #default="scope">
						<el-button size="small" text type="primary" @click="onOpenEditStorage('edit', scope.row)">修改</el-button>
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
		<StorageDialog ref="storageDialogRef" @refresh="getTableData()" />
	</div>
</template>

<script setup lang="ts" name="systemRole">
import { defineAsyncComponent, reactive, onMounted, ref } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import request from '/@/utils/request';

// 引入组件
const StorageDialog = defineAsyncComponent(() => import('./dialog.vue'));

// 定义变量内容
const storageDialogRef = ref();
const state = reactive({
	tableData: {
		data: [] as any,
		total: 0,
		loading: false,
		param: {
			search: '',
			warehouse: '',
			storageArea: '',
			manager: '',
			phone: '',
			pageNum: 1,
			pageSize: 10,
		},
	},
});

// 获取表格数据
const getTableData = () => {
	state.tableData.loading = true;
	request
		.get('/api/storage', {
			params: state.tableData.param,
		})
		.then((res) => {
			if (res.code == 0) {
				state.tableData.data = res.data.records;
				state.tableData.total = res.data.total;
				state.tableData.loading = false;
			} else {
				ElMessage({
					type: 'error',
					message: res.msg,
				});
			}
		});
};

// 打开新增库存弹窗
const onOpenAddStorage = (type: string) => {
	storageDialogRef.value.openDialog(type);
};

// 打开修改库存弹窗
const onOpenEditStorage = (type: string, row: Object) => {
	storageDialogRef.value.openDialog(type, row);
};

// 删除库存
const onRowDel = (row: any) => {
	ElMessageBox.confirm(`此操作将永久删除该库存信息，是否继续?`, '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => {
			request.delete('/api/storage/' + row.id).then((res) => {
				if (res.code == 0) {
					ElMessage({
						type: 'success',
						message: '删除成功！',
					});
					getTableData();
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
				height: 45px;  // 设置行高
			}
			:deep(.el-table__header) {
				th {
					padding: 8px 0;  // 减小表头padding
				}
			}
			:deep(.el-table__cell) {
				padding: 4px 0;  // 减小单元格padding
			}
		}
	}
}
</style> 