
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
            return error;
        }
    },
    async getDaTabaseName() {
        try {
            const response = await api.get('/GetAllDataBaseName');
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async getTableName(dbName) {
        try {
            console.log(dbName)
            const response = await api.get(`/GetAllTableName/${dbName}`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async HandelGenEntity() {
        try {
            const response = await api.get(`/HandelGenEntity`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async HandelGenDTOS() {
        try {
            const response = await api.get(`/HandelGenDTOS`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async HandelGenMapper() {
        try {
            const response = await api.get(`/HandelGenMapper`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async HandelGenRepository() {
        try {
            const response = await api.get(`/HandelGenRepository`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async HandelGenDefineRepositoryToService() {
        try {
            const response = await api.get(`/HandelGenDefineRepositoryToService`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async HandelGenControllerAPIBasic() {
        try {
            const response = await api.get(`/HandelGenControllerAPIBasic`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async HandleGenIndex() {
        try {
            const response = await api.get(`/HandleGenIndex`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    },
    async HandleGenForm() {
        try {
            const response = await api.get(`/HandleGenForm`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async HandleGenAdminRouter() {
        try {
            const response = await api.get(`/HandleGenAdminRouter`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async ModifiersReposotory() {
        try {
            const response = await api.get(`/ModifiersReposotory`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async ModifiersService() {
        try {
            const response = await api.get(`/ModifiersService`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    },
    async ModifiersAPI() {
        try {
            const response = await api.get(`/ModifiersAPI`);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return error;
        }
    }
}
export default GenService