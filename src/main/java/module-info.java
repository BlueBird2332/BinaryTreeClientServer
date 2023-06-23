module com.example.binarytreeclientserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.binarytreeclientserver to javafx.fxml;
    exports com.example.binarytreeclientserver;


    opens com.example.binarytreeclientserver.Server to javafx.fxml;
    exports com.example.binarytreeclientserver.Server;


    opens com.example.binarytreeclientserver.Client to javafx.fxml;
    exports com.example.binarytreeclientserver.Client;

    opens com.example.binarytreeclientserver.ServerData to javafx.fxml;
    exports com.example.binarytreeclientserver.ServerData;




}
