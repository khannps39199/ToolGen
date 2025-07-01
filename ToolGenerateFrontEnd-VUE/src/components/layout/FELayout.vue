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
                                        name="frontEndSourceURL" placeholder="Nhập URL source FrontEndURL to genarate"
                                        v-model="model.frontEndSourceURL" />
                                </div>
                                <div>
                                    <button type="submit" class="btn btn-success w-100">
                                        Select
                                    </button>
                                </div>
                            </form>
                            <div class="button-grid">
                                <button @click="HandelGenEntity" class="btn custom-entity">Entity</button>
                                <button @click="HandelGenDTOS" class="btn custom-dtos">DTOS</button>
                                <button @click="HandelGenMapper" class="btn custom-mapper">Mapper</button>
                                <button @click="HandelGenRepository" class="btn custom-repository">Repository</button>
                                <button @click="HandelGenDefineRepositoryToService"
                                    class="btn custom-define">DefineRepoToService</button>
                                <button @click="HandelGenControllerAPIBasic"
                                    class="btn custom-controller">ControllerAPI</button>
                                <button @click="HandleGenIndex" class="btn custom-index">Generate Index</button>
                                <button @click="HandleGenAdminRouter" class="btn custom-admin-router">Generate
                                    AdminRouter</button>
                                <button @click="HandleGenForm" class="btn custom-form">Generate Form</button>
                                <button @click="ModifiersReposotory" class="btn custom-mod-repo">Modifiers
                                    Reposotory</button>
                                <button @click="ModifiersService"
                                    class="btn custom-mod-service">ModifiersService</button>
                                <button @click="ModifiersAPI" class="btn custom-mod-service">ModifiersAPI</button>
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
async function ModifiersService() {
    await GenService.ModifiersService()
}
async function ModifiersAPI() {
    await GenService.ModifiersAPI()
}

watch(() => model.dbName, async () => {
    listtBL.value = await GenService.getTableName(model.dbName)
})

watch(() => model.tblName, async () => {
    await GenService.setConnect(model)
})

</script>
<style scoped>
form {
    padding: 1rem;
}

.form-label {
    font-weight: 600;
}

.form-control,
.form-select {
    border-radius: 6px;
    transition: border-color 0.2s ease, box-shadow 0.2s ease;
}

.form-control:focus,
.form-select:focus {
    border-color: #80bdff;
    box-shadow: 0 0.15rem rgba(0, 123, 255, 0.25);
}

.card {
    border-radius: 10px;
    overflow: hidden;
}

.card-header {
    border-bottom: 1px solid rgba(0, 0, 0, 0.125);
}

button[type="submit"] {
    margin-top: 1rem;
    font-size: 1.1rem;
    padding: 10px;
}

.button-grid {
    /* display: flex;
    flex-wrap: wrap;
    gap: 12px;
    padding: 16px; */
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 1rem;
    margin-top: 1rem;
}

.btn {
    padding: 10px 16px;
    font-weight: bold;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.btn:hover {
    transform: scale(1.05);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* 🎨 Individual button colors */
.custom-entity {
    background-color: #007BFF;
    /* Blue */
}

.custom-dtos {
    background-color: #6F42C1;
    /* Purple */
}

.custom-mapper {
    background-color: #20C997;
    /* Teal */
}

.custom-repository {
    background-color: #17A2B8;
    /* Cyan */
}

.custom-define {
    background-color: #FD7E14;
    /* Orange */
}

.custom-controller {
    background-color: #28A745;
    /* Green */
}

.custom-index {
    background-color: #6610f2;
    /* Indigo */
}

.custom-admin-router {
    background-color: #E83E8C;
    /* Pink */
}

.custom-form {
    background-color: #FFC107;
    /* Amber */
    color: #212529;
    /* dark text */
}

.custom-mod-repo {
    background-color: #DC3545;
    /* Red */
}

.custom-mod-service {
    background-color: #343A40;
    /* Dark Gray */
}

.custom-mod-service:hover {
    background-color: #495057;
}
</style>
