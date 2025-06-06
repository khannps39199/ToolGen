
import axios from 'axios';
const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
});
const GenService = {
    async setConnect({
        userName, password, dbName, tblName, backEndSourceURL,
        frontEndSourceURL
    }) {
        try {
            const response = await api.post('/ConnectSQL', {
                userName, password, dbName, tblName, backEndSourceURL,
                frontEndSourceURL
            });
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async getDaTabaseName() {
        try {
            const response = await api.get('/GetAllDataBaseName');
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async getTableName(dbName) {
        try {
            console.log(dbName)
            const response = await api.get(`/GetAllTableName/${dbName}`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandelGenEntity() {
        try {
            const response = await api.get(`/HandelGenEntity`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandelGenDTOS() {
        try {
            const response = await api.get(`/HandelGenDTOS`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandelGenMapper() {
        try {
            const response = await api.get(`/HandelGenMapper`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandelGenRepository() {
        try {
            const response = await api.get(`/HandelGenRepository`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandelGenDefineRepositoryToService() {
        try {
            const response = await api.get(`/HandelGenDefineRepositoryToService`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandelGenControllerAPIBasic() {
        try {
            const response = await api.get(`/HandelGenControllerAPIBasic`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandleGenIndex() {
        try {
            const response = await api.get(`/HandleGenIndex`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandleGenForm() {
        try {
            const response = await api.get(`/HandleGenForm`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandleGenAdminRouter() {
        try {
            const response = await api.get(`/HandleGenAdminRouter`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    }
}
export default GenService