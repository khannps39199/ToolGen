<template>

    <body>
        <div class="container-xxl mt-5">
            <div class="row justify-content-center">
                <div class="col-xxl-8 col-xl-8 col-lg-10 col-md-12">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">Connect to SQL</h5>
                        </div>
                        <div class="card-body">
                            <form @submit.prevent="handleConnect()">
                                <dic class="grid">
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
                                </dic>
                                <div class="grid">
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
                            <div class="grid button-holder">
                                <button @click="HandelGenEntity" class="btn custom-entity">Entity</button>
                                <button @click="HandelGenDTOS" class="btn custom-dtos">DTOS</button>
                                <button @click="HandelGenMapper" class="btn custom-mapper">Mapper</button>
                                <button @click="HandelGenRepository" class="btn custom-repository">Repository</button>
                                <button @click="HandelGenDefineRepositoryToService" class="btn custom-define">Define
                                    Repository Service</button>
                                <button @click="HandelGenControllerAPIBasic" class="btn custom-controller">Controller
                                    API</button>
                                <button @click="HandleGenIndex" class="btn custom-index">Generate Index</button>
                                <button @click="HandleGenAdminRouter" class="btn custom-admin-router">Generate
                                    AdminRouter</button>
                                <button @click="HandleGenForm" class="btn custom-form">Generate Form</button>
                                <button @click="ModifiersReposotory" class="btn custom-mod-repo">Modifiers
                                    Reposotory</button>
                                <button @click="ModifiersService" class="btn custom-mod-service">Modifiers
                                    Service</button>
                                <button @click="ModifiersAPI" class="btn custom-mod-service">Modifiers API</button>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="action-holder col-xxl-4 col-xl-8 col-lg-10 col-md-12 ">
                    <div class="text-holder">
                        <h2>Action Log</h2>
                    </div>
                    <ul class="list-group">
                        <li v-for="(log, index) in logActionList" :key="index" class="list-group-item">
                            {{ log }}
                        </li>
                    </ul>
                    <!-- <div class="btn clear-actions"> -->
                    <button class="btn clear-actions" @click="clearLogs">Clear action logs</button>
                    <!-- </div> -->
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
const logActionList = ref([])
function clearLogs() {
    logActionList.value = [];
}
async function handleConnect() {
    listDB.value = await GenService.setConnect(model)
    logActionList.value.push("Connected to database: " + model.dbName);
}
async function HandelGenEntity() {
    const responseAction = await GenService.HandelGenEntity()
    logActionList.value.push(responseAction)
}
async function HandelGenDTOS() {
    const responseAction = await GenService.HandelGenDTOS()
    logActionList.value.push(responseAction)
}
async function HandelGenMapper() {
    const responseAction = await GenService.HandelGenMapper()
    logActionList.value.push(responseAction)
}
async function HandelGenRepository() {
    const responseAction = await GenService.HandelGenRepository()
    logActionList.value.push(responseAction)
}
async function HandelGenDefineRepositoryToService() {
    const responseAction = await GenService.HandelGenDefineRepositoryToService()
    logActionList.value.push(responseAction)
}
async function HandelGenControllerAPIBasic() {
    const responseAction = await GenService.HandelGenControllerAPIBasic()
    logActionList.value.push(responseAction)
}
async function HandleGenAdminRouter() {
    const responseAction = await GenService.HandleGenAdminRouter()
    logActionList.value.push(responseAction)
}
async function HandleGenIndex() {
    const responseAction = await GenService.HandleGenIndex()
    logActionList.value.push(responseAction)
}
async function HandleGenForm() {
    const responseAction = await GenService.HandleGenForm()
    logActionList.value.push(responseAction)
}

async function ModifiersReposotory() {
    const responseAction = await GenService.ModifiersReposotory()
    logActionList.value.push(responseAction)
}
async function ModifiersService() {
    const responseAction = await GenService.ModifiersService()
    logActionList.value.push(responseAction)
}
async function ModifiersAPI() {
    const responseAction = await GenService.ModifiersAPI()
    logActionList.value.push(responseAction)
}

watch(() => model.dbName, async () => {
    listtBL.value = await GenService.getTableName(model.dbName)
    logActionList.value.push("Provided Database name: " + model.dbName);
})

watch(() => model.tblName, async () => {
    await GenService.setConnect(model)
    logActionList.value.push("Provided table name: " + model.tblName);
})

</script>
<style scoped>
form {}

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

.text-holder {
    text-align: center;
}


.grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
}

.clear-actions {
    display: block;
    margin: 1rem auto 0 auto;
    /* top auto bottom auto */
    padding: 8px 16px;
    font-weight: bold;
    border-radius: 6px;
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

/* .action-holder {
    background-color: #f8f9fa;
border-radius: 10px;
box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

*/
.action-holder {
    color: white;
    position: relative;
    font-style: bold;
    background-color: #9ac6f0;
    /* Light background */
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.list-group {
    max-height: 300px;
    overflow-y: auto;
    font-family: monospace;
    font-size: 0.95rem;
    border-radius: 8px;
    margin-bottom: 1rem;
}

.list-group-item {
    background-color: #ffffff;
    border: 1px solid #dee2e6;
    transition: background-color 0.2s ease;
}

.list-group-item:hover {
    background-color: #f1f3f5;
}

.action-holder button {
    background-color: #dc3545;
    color: white;
    border: none;
    padding: 8px 16px;
    font-weight: bold;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.2s ease, transform 0.2s ease;
}

.action-holder button:hover {
    background-color: #c82333;
    transform: scale(1.05);
}

ul.list-group {
    max-height: 250px;
    overflow-y: auto;
    margin-top: 1rem;
    font-family: monospace;
    font-size: 0.95rem;
}

.btn:hover {
    transform: scale(1.05);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.button-holder {
    padding-top: 1rem;
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
