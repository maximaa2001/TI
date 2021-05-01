package by.bsuir;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_file;

    @FXML
    private TextField start_state_field;

    @FXML
    private Button btn_encrypt;

    @FXML
    private Button btn_decrypt;

    @FXML
    private TextArea plain_field;

    @FXML
    private TextArea key_field;

    @FXML
    private TextArea encrypted_field;

    private File file;

    private byte[] plainTextBytes;

    private byte[] encryptTextBytes;

    @FXML
    void initialize() {
        btn_file.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    InputStream inputStream = new FileInputStream(file);
                    plainTextBytes = new byte[inputStream.available()];
                    inputStream.read(plainTextBytes);
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String plain_bits = "";
                for (int i = 0; i < plainTextBytes.length; i++) {
                    if (plain_bits.length() < 4991) {
                        String str = String.format("%8s", Integer.toBinaryString(plainTextBytes[i] & 0xFF));
                        str = str.replace(' ', '0');
                        plain_bits += str + "  ";
                    }
                }
                plain_field.setText(plain_bits);
            }
        });

        btn_encrypt.setOnMouseClicked(event -> {
            String state = start_state_field.getText();
            state = changeKey(state);
            if (correct(state) && file != null) {
                encrypt(state);
            }else {
                try {
                    Message message = new Message("Неверное состояние регистра или не выбран файл");
                    message.display();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btn_decrypt.setOnMouseClicked(event -> {
            String state = start_state_field.getText();
            state = changeKey(state);
            if (correct(state) && file != null) {
                encrypt(state);
            }else {
                try {
                    Message message = new Message("Неверное состояние регистра или не выбран файл");
                    message.display();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void encrypt(String state) {
        Register register = new Register((int) Long.parseLong(state, 2));
        encryptTextBytes = new byte[plainTextBytes.length];
        String key_bits = "";
        for (int i = 0; i < plainTextBytes.length; i++) {
            byte key = (byte) register.move();
            if (key_bits.length() < 4991) {
                String str = String.format("%8s", Integer.toBinaryString(key & 0xFF));
                str = str.replace(' ', '0');
                key_bits += str + "  ";
            }
            encryptTextBytes[i] = (byte) (plainTextBytes[i] ^ key);
        }
        key_field.setText(key_bits);

        String encrypt_bits = "";
        for (int i = 0; i < encryptTextBytes.length; i++) {
            if (encrypt_bits.length() < 4991) {
                String str = String.format("%8s", Integer.toBinaryString(encryptTextBytes[i] & 0xFF));
                str = str.replace(' ', '0');
                encrypt_bits += str + "  ";
            }
        }
        encrypted_field.setText(encrypt_bits);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(encryptTextBytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean correct(String key){
        if(key.length() == 32){
            return true;
        }
        return false;
    }

    private String changeKey(String key){
        for (int i = 0; i < key.length() ; i++) {
            if(key.charAt(i) == '0' || key.charAt(i) == '1'){
                continue;
            }else {
                key = key.substring(0,i) + key.substring(i+1);
                i--;
            }
        }
        return key;
    }
}
