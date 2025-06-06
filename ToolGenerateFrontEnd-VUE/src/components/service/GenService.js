
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
            const response = await api.get(`/GetAllTableName/${dbName}`);
            console.log('Response:', response.data);
            return response.data;
        } catch (error) {
            console.error('Connection failed:', error);
            return null;
        }
    }
}
export default GenService