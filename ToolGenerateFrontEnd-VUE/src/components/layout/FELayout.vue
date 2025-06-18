<template>

    <body>
        <div class="container-xxl mt-5">
            <div class="row justify-content-center">
                <div class="col-xxl-6">
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
                            <div class="button-grid">
                                <button @click="HandelGenEntity" class="btn btn-primary">Entity</button>
                                <button @click="HandelGenDTOS" class="btn btn-secondary">DTOS</button>
                                <button @click="HandelGenMapper" class="btn btn-success">Mapper</button>
                                <button @click="HandelGenRepository" class="btn btn-danger">Repository</button>
                                <button @click="HandelGenDefineRepositoryToService"
                                    class="btn btn-warning">DefineRepoToService</button>
                                <button @click="HandelGenControllerAPIBasic" class="btn btn-info">ControllerAPI</button>
                                <button @click="HandleGenIndex" class="btn btn-dark">Generate Index</button>
                                <button @click="HandleGenAdminRouter" class="btn btn-warning">Generate
                                    AdminRouter</button>
                                <button @click="HandleGenForm" class="btn btn-light text-dark">Generate Form</button>
                                <button @click="ModifiersReposotory" class="btn btn-light-gray text-dark">Modifiers
                                    Reposotory</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</template>
<script setup>
import { reactive, ref, watch, watchEffect, withModifiers } from 'vue'
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
async function HandelGenEntity() {
    await GenService.HandelGenEntity()
}
async function HandelGenDTOS() {
    await GenService.HandelGenDTOS()
}
async function HandelGenMapper() {
    await GenService.HandelGenMapper()
}
async function HandelGenRepository() {
    await GenService.HandelGenRepository()
}
async function HandelGenDefineRepositoryToService() {
    await GenService.HandelGenDefineRepositoryToService()
}
async function HandelGenControllerAPIBasic() {
    await GenService.HandelGenControllerAPIBasic()
}
async function HandleGenAdminRouter() {
    await GenService.HandleGenAdminRouter()
}
async function HandleGenIndex() {
    await GenService.HandleGenIndex()
}
async function HandleGenForm() {
    await GenService.HandleGenForm()
}

async function ModifiersReposotory() {
    await GenService.ModifiersReposotory()
}

watch(() => model.dbName, async () => {
    listtBL.value = await GenService.getTableName(model.dbName)
})

watch(() => model.tblName, async () => {
    await GenService.setConnect(model)
})

</script>
<style scoped>
.button-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 1rem;
    margin-top: 1rem;
}
</style>
