package by.bsuir;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Message {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label label_message;

    @FXML
    private Button btn_close;

    private final Stage stage;
    private  final String message;

    public Message(String message) throws IOException {
        this.message = message;
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Message.fxml"));
        loader.setController(this);
        stage.setScene(new Scene(loader.load()));
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    public void display(){
        stage.show();
    }


    @FXML
    void initialize()  {
        label_message.setText(message);
        btn_close.setOnAction(event ->{
            stage.close();
        });

    }
}
