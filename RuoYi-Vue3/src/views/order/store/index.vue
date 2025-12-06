<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="门店名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入门店名称"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="门店编码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入门店编码"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="enabled">
        <el-select v-model="queryParams.enabled" placeholder="状态" clearable style="width: 200px">
          <el-option
            v-for="dict in sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="storeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="门店名称" align="center" prop="name" show-overflow-tooltip />
      <el-table-column label="门店编码" align="center" prop="code" width="160" />
      <el-table-column label="地址" align="center" prop="address" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="enabled" width="120">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.enabled" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="门店名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入门店名称" />
        </el-form-item>
        <el-form-item label="门店编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入门店编码" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-radio-group v-model="form.enabled">
            <el-radio-button v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">
              {{ dict.label }}
            </el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="OrderStore">
import { ElMessage, ElMessageBox } from 'element-plus'
import { reactive, ref, toRefs } from 'vue'
import { listStore, getStore, delStore, addStore, updateStore } from '@/api/order/store'
import { useDict } from '@/hooks/useDict'

const { sys_normal_disable } = useDict('sys_normal_disable')

const storeList = ref([])
const open = ref(false)
const loading = ref(false)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref('')
const queryRef = ref()
const formRef = ref()

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    code: undefined,
    enabled: undefined
  },
  rules: {
    name: [{ required: true, message: '门店名称不能为空', trigger: 'blur' }],
    code: [{ required: true, message: '门店编码不能为空', trigger: 'blur' }],
    enabled: [{ required: true, message: '请选择状态', trigger: 'change' }]
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询门店列表 */
function getList() {
  loading.value = true
  listStore(queryParams.value).then(response => {
    storeList.value = response.rows
    total.value = response.total
  }).finally(() => {
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: undefined,
    name: undefined,
    code: undefined,
    address: undefined,
    enabled: '1'
  }
  formRef.value && formRef.value.resetFields()
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryRef.value.resetFields()
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = '新增门店'
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const storeId = row.id || ids.value[0]
  getStore(storeId).then(response => {
    form.value = response.data
    open.value = true
    title.value = '修改门店'
  })
}

/** 提交按钮 */
function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) return
    if (form.value.id) {
      updateStore(form.value).then(() => {
        ElMessage.success('修改成功')
        open.value = false
        getList()
      })
    } else {
      addStore(form.value).then(() => {
        ElMessage.success('新增成功')
        open.value = false
        getList()
      })
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const delIds = row?.id ? [row.id] : ids.value
  if (!delIds.length) return
  ElMessageBox.confirm(`是否确认删除编号为"${delIds.join(', ')}"的门店?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delStore(delIds.join(','))
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  })
}

getList()
</script>
