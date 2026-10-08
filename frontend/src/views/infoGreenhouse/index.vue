<template>
	<div class="system-role-container layout-padding">
		<div class="system-role-padding layout-padding-auto layout-padding-view">
			<div class="system-user-search mb15">
				<el-input v-model="state.tableData.param.search" size="default" placeholder="请输入温室名称" style="max-width: 180px"> </el-input>
				<el-input v-model="state.tableData.param.cropType" size="default" placeholder="请输入作物类型" class="ml10" style="max-width: 180px"> </el-input>
				<el-input v-model="state.tableData.param.growthStatus" size="default" placeholder="请输入生长状态" class="ml10" style="max-width: 180px"> </el-input>
				<el-input v-model="state.tableData.param.manager" size="default" placeholder="请输入负责人" class="ml10" style="max-width: 180px"> </el-input>
				<el-button size="default" type="primary" class="ml10" @click="getTableData()">
					<el-icon>
						<ele-Search />
					</el-icon>
					查询
				</el-button>
				<el-button size="default" type="success" class="ml10" @click="onOpenAddGreenhouse('add')">
					<el-icon>
						<ele-FolderAdd />
					</el-icon>
					添加
				</el-button>
			</div>
			<el-table :data="state.tableData.data" v-loading="state.tableData.loading" style="width: 100%">
				<el-table-column prop="num" label="序号" width="80" align="center" />
				<el-table-column prop="greenhouseName" label="温室名称" show-overflow-tooltip width="100" align="center" />
				<el-table-column prop="cropType" label="作物类型" show-overflow-tooltip width="100" align="center" />
				<el-table-column prop="quantity" label="数量" width="100" align="center" />
				<el-table-column prop="growthStatus" label="生长状态" show-overflow-tooltip width="100" align="center" />
				<el-table-column prop="temperature" label="温度(℃)" width="100" align="center" />
				<el-table-column prop="airHumidity" label="空气湿度(%)" width="105" align="center" />
				<el-table-column prop="soilHumidity" label="土壤湿度(%)" width="110" align="center" />
				<el-table-column prop="co2Concentration" label="CO2浓度(ppm)" width="120" align="center" />
				<el-table-column prop="soilPh" label="土壤pH" width="80" align="center" />
				<el-table-column prop="lightIntensity" label="光照强度(lux)" width="110" align="center" />
				<!-- <el-table-column prop="supplementaryLight" label="补光状态" width="80" align="center" />
				<el-table-column prop="ventilation" label="通风状态" width="80" align="center" />
				<el-table-column prop="irrigation" label="灌溉状态" width="80" align="center" /> -->
				<el-table-column prop="manager" label="负责人" show-overflow-tooltip width="80" align="center" />
				<el-table-column prop="recordTime" label="记录时间" width="160" align="center" />
				<el-table-column label="操作" width="150" fixed="right" align="center">
					<template #default="scope">
						<el-button size="small" text type="primary" @click="onOpenEditGreenhouse('edit', scope.row)">修改</el-button>
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
		<GreenhouseDialog ref="greenhouseDialogRef" @refresh="getTableData()" />
	</div>
</template>

<script setup lang="ts" name="systemRole">
import { defineAsyncComponent, reactive, onMounted, ref } from 'vue';
import { ElMessageBox, ElMessage } from 'element-plus';
import request from '/@/utils/request';

// 引入组件
const GreenhouseDialog = defineAsyncComponent(() => import('./dialog.vue'));

// 定义变量内容
const greenhouseDialogRef = ref();
const state = reactive({
	tableData: {
		data: [] as any,
		total: 0,
		loading: false,
		param: {
			search: '',
			cropType: '',
			growthStatus: '',
			manager: '',
			recordTime: '',
			pageNum: 1,
			pageSize: 10,
		},
	},
});

// 获取表格数据
const getTableData = () => {
	state.tableData.loading = true;
	request
		.get('/api/greenhouse', {
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

// 打开新增温室弹窗
const onOpenAddGreenhouse = (type: string) => {
	greenhouseDialogRef.value.openDialog(type);
};

// 打开修改温室弹窗
const onOpenEditGreenhouse = (type: string, row: Object) => {
	greenhouseDialogRef.value.openDialog(type, row);
};

// 删除温室
const onRowDel = (row: any) => {
	ElMessageBox.confirm(`此操作将永久删除该温室信息，是否继续?`, '提示', {
		confirmButtonText: '确认',
		cancelButtonText: '取消',
		type: 'warning',
	})
		.then(() => {
			request.delete('/api/greenhouse/' + row.id).then((res) => {
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
		}
	}
}
</style>