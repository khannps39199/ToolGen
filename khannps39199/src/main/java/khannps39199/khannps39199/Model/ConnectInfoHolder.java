package khannps39199.khannps39199.Model;

import org.springframework.stereotype.Component;

@Component
public class ConnectInfoHolder {
    
    private ConnectInfo connectInfo;

    public ConnectInfo getConnectInfo() {
        return connectInfo;
    }

    public void setConnectInfo(ConnectInfo connectInfo) {
        this.connectInfo = connectInfo;
    }
}

