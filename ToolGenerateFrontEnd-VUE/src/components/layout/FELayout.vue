<template>

    <body>
        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">Connect to SQL</h5>
                        </div>
                        <div class="card-body">
                            <form @submit.prevent="handleConnect()">
                                <div class="mb-3">
                                    <label for="username" class="form-label">Username</label>
                                    <input type="text" class="form-control" id="username" name="username"
                                        v-model="model.userName" />
                                </div>

                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="text" class="form-control" id="password" name="password"
                                        v-model="model.password" />
                                </div>

                                <div class="mb-3">
                                    <label for="database" class="form-label">Database</label>
                                    <select class="form-select" name="databaseSelect" id="database"
                                        v-model="model.dbName">
                                        <option value="master">master</option>
                                        <option v-for="item in listDB" :key="item" :value="item">
                                            {{ item }}
                                        </option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="tableSelect" class="form-label">Table</label>
                                    <select class="form-select" name="tableSelect" id="tableSelect"
                                        v-model="model.tblName">
                                        <option selected value="All">All</option>
                                        <option v-for="item in listtBL" :key="item" :value="item">
                                            {{ item }}
                                        </option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label for="backendsourceURl" class="form-label">backendsourceURl</label>
                                    <input type="text" class="form-control" id="backendsourceURl"
                                        name="backendsourceURl" placeholder="Nhập URL source BackEndURL to genarate"
                                        v-model="model.backEndSourceURL" />
                                </div>
                                <div class="mb-3">
                                    <label for="frontEndSourceURL" class="form-label">frontEndSourceURL</label>
                                    <input type="text" class="form-control" id="frontEndSourceURL"
                                        name="frontEndSourceURL" placeholder="" v-model="model.frontEndSourceURL" />
                                </div>
                                <div>
                                    <button type="submit" class="btn btn-success w-100">
                                        Select
                                    </button>
                                </div>
                            </form>
                            <div class="row mt-2">
                                <button @click="handelGenEntity" class="btn btn-success  col-2">
                                    Entity
                                </button>
                                <button @click="handelGenDTOS" class="btn btn-success  col-2">
                                    DTOS
                                </button>
                                <button @click="handelGenMapper" class="btn btn-success  col-2">
                                    handelGenMapper
                                </button>
                                <button @click="handelGenRepository" class="btn btn-success  col-2">
                                    Repository
                                </button>
                                <button @click="handelGenDefineRepositoryToService" class="btn btn-success  col-2">
                                    DefineRepositoryToService
                                </button>
                                <button @click="handelGenControllerAPIBasic" class="btn btn-success  col-2">
                                    ControllerAPIBasic
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</template>
<script setup>
import { reactive, ref, watch, watchEffect } from 'vue'
import GenService from '../service/GenService'
const model = reactive({
    userName: 'sa',
    password: '2609',
    dbName: 'master',
    tblName: 'All',
    backEndSourceURL: '',
    frontEndSourceURL: '',
}
);
const isConnected = ref(false);
const listDB = ref([])
const listtBL = ref([])
async function handleConnect() {
    listDB.value = await GenService.setConnect(model)
    console.log(model.dbName)
}
async function handelGenEntity() {
}
async function handelGenDTOS() {
}
async function handelGenMapper() {
}
async function handelGenRepository() {
}
async function handelGenDefineRepositoryToService() {
}
async function handelGenControllerAPIBasic() {
}

watch(() => model.dbName, async () => {
    listtBL.value = await GenService.getTableName(model.dbName)
})
</script>

<style scoped></style>
