package by.bsuir;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_choose;

    @FXML
    private CheckBox check_rail;

    @FXML
    private CheckBox check_grid;

    @FXML
    private CheckBox check_vig;

    @FXML
    private TextField key_field;

    @FXML
    private CheckBox check_encrypt;

    @FXML
    private CheckBox check_decrypt;

    @FXML
    private Button btn_start;

    @FXML
    private Label label_text;

    private File file;
    private StringBuilder lines;
    private String firstText;

    @FXML
    void initialize() {
        btn_choose.setOnAction(event ->{
            firstText = "";
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text","txt"));
            file = fileChooser.showOpenDialog(null);
            if(file != null) {
                lines = new StringBuilder();
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;
                try {
                    fileReader = new FileReader(file);
                    String str;
                    bufferedReader = new BufferedReader(fileReader);
                    while ((str = bufferedReader.readLine()) != null) {
                        lines.append(str.replaceAll(" ", ""));
                    }
                    fileReader.close();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < lines.length(); i++) {
                    firstText += lines.charAt(i);
                }
            }
        });
        btn_start.setOnAction(event -> {
            if(file == null){
               someMessage("Выберете файл!");
            }
            else if(!check_rail.isSelected() && !check_grid.isSelected() && !check_vig.isSelected()){
              someMessage("Выберете способ шифрования!");
            }
            else if ((check_rail.isSelected() && check_grid.isSelected()) || (check_rail.isSelected() && check_vig.isSelected()) ||
                    (check_grid.isSelected() && check_vig.isSelected()) || (check_rail.isSelected() && check_vig.isSelected() &&
                    check_vig.isSelected())) {
               someMessage("Выберете только один способ шифрования!");
            }
            else if(!check_encrypt.isSelected() && !check_decrypt.isSelected()){
                someMessage("Будем шифровать или расшифрововать?");
            }
            else if(check_encrypt.isSelected() && check_decrypt.isSelected()){
                someMessage("Будем шифровать или расшифрововать?");
            }
            else if(check_rail.isSelected() && check_encrypt.isSelected()){ // Изгородь шифрование
                String key = key_field.getText();
                String numberKey = "";
                if(key.length() == 0){
                    someMessage("Введите ключ");
                }
                else {
                    for (int i = 0; i < key.length(); i++) {
                        String a = "";
                        a += key.charAt(i);
                       if(isDigit(a)){
                           numberKey += a;
                       }
                    }
                        for (int i = 0; i < firstText.length(); i++) {
                            if ((firstText.charAt(i) >= 1040 && firstText.charAt(i) <= 1103) || firstText.charAt(i) == 1025 ||
                                    firstText.charAt(i) == 1105) {
                                continue;
                            } else {
                                firstText = firstText.substring(0, i) + firstText.substring(i + 1);
                                i--;
                            }
                        }
                        if(firstText.length() == 0){
                            someMessage("Нет корректных символов для шифрования");
                        }
                        else {
                            if(numberKey.length() == 0){
                                someMessage("Ключ должет содержать цифру");
                            }
                            else if (Integer.parseInt(numberKey) <= 1) {
                                someMessage("Ключ должен быть больше 1");
                            } else if (Integer.parseInt(numberKey) >= firstText.length()) {
                                someMessage("Слишком большой ключ");
                            } else {
                                String encryptText = encryptRail(firstText, numberKey);
                                writeFile(encryptText);
                                label_text.setText(encryptText);
                            }
                        }
                }
            }
            else if(check_rail.isSelected() && check_decrypt.isSelected()){ // изгородь дешифрование
                String key = key_field.getText();
                String numberKey = "";
                if(key.length() == 0){
                    someMessage("Введите ключ");
                }
                else {
                    for (int i = 0; i < key.length(); i++) {
                        String a = "";
                        a += key.charAt(i);
                        if(isDigit(a)){
                            numberKey += a;
                        }
                    }
                        for (int i = 0; i < firstText.length(); i++) {
                            if ((firstText.charAt(i) >= 1040 && firstText.charAt(i) <= 1103) || firstText.charAt(i) == 1025 ||
                                    firstText.charAt(i) == 1105) {
                                continue;
                            } else {
                                firstText = firstText.substring(0, i) + firstText.substring(i + 1);
                                i--;
                            }
                        }
                        if(firstText.length() == 0){
                            someMessage("Нет корректных символов для дешифрования");
                        }
                        else {
                            if(numberKey.length() == 0){
                                someMessage("Ключ должет содержать цифру");
                            }
                            else if (Integer.parseInt(numberKey) <= 1) {
                                someMessage("Ключ должен быть больше 1");
                            } else if (Integer.parseInt(numberKey) >= firstText.length()) {
                                someMessage("Слишком большой ключ");
                            } else {
                                String decryptText = decryptRail(firstText, numberKey);
                                writeFile(decryptText);
                                label_text.setText(decryptText);
                            }
                        }
                }
            }
            else if(check_grid.isSelected() && check_encrypt.isSelected()) { // решетка шифрование
                String key = key_field.getText();
                if (!key.equals("")) {
                    someMessage("При шифровании решеткой не нужно вводить ключ");
                } else {
                    for (int i = 0; i < firstText.length(); i++) {
                        if ((firstText.charAt(i) >= 65 && firstText.charAt(i) <= 90) || (firstText.charAt(i) >= 97 &&
                                firstText.charAt(i) <= 122)) {
                            continue;
                        } else {
                            firstText = firstText.substring(0, i) + firstText.substring(i + 1);
                            i--;
                        }
                    }
                    if (firstText.length() == 0) {
                        someMessage("Нет корректных символов для шифрования");
                    }
                    else {
                        String encryptText = encryptGrid(firstText);
                        writeFile(encryptText);
                        label_text.setText(encryptText);
                    }
                }
            }
            else if(check_grid.isSelected() && check_decrypt.isSelected()){ // решетка дешифрование
                String key = key_field.getText();
                if(!key.equals("")){
                   someMessage("При шифровании решеткой не нужно вводить ключ");
                }else {
                    for (int i = 0; i < firstText.length(); i++) {
                        if ((firstText.charAt(i) >= 65 && firstText.charAt(i) <= 90) || (firstText.charAt(i) >= 97 &&
                                firstText.charAt(i) <= 122)) {
                            continue;
                        } else {
                            firstText = firstText.substring(0, i) + firstText.substring(i + 1);
                            i--;
                        }
                    }
                    if(firstText.length() == 0){
                        someMessage("Нет корректных символов для дешифрования");
                    }
                    else {
                        String decryptText = decryptGrid(firstText);
                        writeFile(decryptText);
                        label_text.setText(decryptText);
                    }
                }
            }
            else if(check_vig.isSelected() && check_encrypt.isSelected()){ // Виженер шифрование
                String key = key_field.getText();
                int count = key.length();
                if(key.equals("")){
                    someMessage("Введите ключ");
                }
                else {
                    for (int i = 0; i < firstText.length() ; i++) {
                        if((firstText.charAt(i) >= 1040 && firstText.charAt(i) <= 1103) || firstText.charAt(i) == 1025 ||
                                firstText.charAt(i) == 1105){
                            continue;
                        }
                        else {
                            firstText = firstText.substring(0,i) + firstText.substring(i+1);
                            i--;
                        }
                    }
                    int temp = 0;
//                    for (int i = 0; i < key.length() ; i++) {
//                        if((key.charAt(i) >= 1040 && key.charAt(i) <= 1103) || key.charAt(i) == 1025 ||
//                                key.charAt(i) == 1105){
//                            temp++;
//                        }
//                    }
                    for (int i = 0; i < key.length() ; i++) {
                        if((key.charAt(i) >= 1040 && key.charAt(i) <= 1103) || key.charAt(i) == 1025 ||
                                key.charAt(i) == 1105){
                            continue;
                        }
                        else {
                            key = key.substring(0,i) + key.substring(i+1);
                            i--;
                        }
                    }
                    //можно вставить проверку на длину ключа
                    if(key.length() > 0){
                        if(firstText.length() == 0){
                            someMessage("Нет корректных символов для шифрования");
                        }
                        else {
                            String encryptText = encryptVig(firstText,key);
                            writeFile(encryptText);
                            label_text.setText(encryptText);
                        }
                    }
                    else {
                        someMessage("Ключ должен содержать русские буквы");
                    }
                }
            }
            else if(check_vig.isSelected() && check_decrypt.isSelected()){ // Виженер дешифрование
                String key = key_field.getText();
                int count = key.length();
                if(key.equals("")){
                    someMessage("Введите ключ");
                }else {
                    for (int i = 0; i < firstText.length() ; i++) {
                        if((firstText.charAt(i) >= 1040 && firstText.charAt(i) <= 1103) || firstText.charAt(i) == 1025 ||
                                firstText.charAt(i) == 1105){
                            continue;
                        }
                        else {
                            firstText = firstText.substring(0,i) + firstText.substring(i+1);
                            i--;
                        }
                    }
//                    int temp = 0;
//                    for (int i = 0; i < key.length() ; i++) {
//                        if((key.charAt(i) >= 1040 && key.charAt(i) <= 1103) || key.charAt(i) == 1025 ||
//                                key.charAt(i) == 1105){
//                            temp++;
//                        }
//                    }
                    for (int i = 0; i < key.length() ; i++) {
                        if((key.charAt(i) >= 1040 && key.charAt(i) <= 1103) || key.charAt(i) == 1025 ||
                                key.charAt(i) == 1105){
                            continue;
                        }
                        else {
                            key = key.substring(0,i) + key.substring(i+1);
                            i--;
                        }
                    }

                    if(key.length() > 0){
                        if(firstText.length() == 0){
                            someMessage("Нет корректных символов для дешифрования");
                        }
                        else {
                            String decryptText = decryptVig(firstText,key);
                            writeFile(decryptText);
                            label_text.setText(decryptText);
                        }
                    }else {
                        someMessage("Ключ должен содержать русские буквы");
                    }
                }
            }
            file = null;
        });
    }

    private String encryptRail(String message, String firstKey) {
        int key = Integer.parseInt(firstKey);
        char[][] rail = new char[key][message.length()];

        for (int i = 0; i < key; i++) {
            for (int j = 0; j < message.length(); j++) {
                rail[i][j] = '.';
            }
        }
        int row = 0;
        int side = 0;
        for (int i = 0; i < message.length(); i++) {
            if (side == 0) {
                rail[row][i] = message.charAt(i);
                row++;
                if (row == key) {
                    side = 1;
                    row--;
                }
            } else if (side == 1) {
                row--;
                rail[row][i] = message.charAt(i);
                if (row == 0) {
                    side = 0;
                    row++;
                }
            }
        }

        String encryptText = "";
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < message.length(); j++) {
                encryptText += rail[i][j];
            }
        }
        encryptText = encryptText.replaceAll("\\.", "");
        return encryptText;
    }

    private String decryptRail(String message, String firstKey) {
        int key = Integer.parseInt(firstKey);
        char[][] rail = new char[key][message.length()];

        for (int i = 0; i < key; i++) {
            for (int j = 0; j < message.length(); j++) {
                rail[i][j] = '.';
            }
        }
        int row = 0;
        int side = 0;
        for (int i = 0; i < message.length(); i++) {
            if (side == 0) {
                rail[row][i] = message.charAt(i);
                row++;
                if (row == key) {
                    side = 1;
                    row--;
                }
            } else if (side == 1) {
                row--;
                rail[row][i] = message.charAt(i);
                if (row == 0) {
                    side = 0;
                    row++;
                }
            }
        }

        int index = 0;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < message.length(); j++) {
                if (rail[i][j] == '.') {
                    continue;
                } else {
                    rail[i][j] = message.charAt(index);
                    index++;
                }
            }
        }

        row = 0;
        side = 0;
        String decryptText = "";
        for (int i = 0; i < message.length(); i++) {
            if (side == 0) {
                decryptText += rail[row][i];
                row++;
                if (row == key) {
                    side = 1;
                    row--;
                }
            } else if (side == 1) {
                row--;
                decryptText += rail[row][i];
                if (row == 0) {
                    side = 0;
                    row++;
                }
            }
        }
        return decryptText;
    }

    private String encryptGrid(String message) {
        ArrayList<String> firstWords = new ArrayList<>();
        int index = 0;
        String temp = "";
        while (index < message.length()) {
            temp += message.charAt(index);
            if (temp.length() == 16) {
                firstWords.add(temp);
                temp = "";
            }
            index++;
        }
        if (temp.length() != 0) {
            firstWords.add(temp);
        }

        String encryptText = "";
        boolean isNext;
        for (int size = 0; size < firstWords.size(); size++) {
            String[][] arr = new String[][]{
                    {"1", "2", "3", "1"},
                    {"3", "4", "4", "2"},
                    {"2", "4", "4", "3"},
                    {"1", "3", "2", "1"}
            };
            String[][] key = new String[][]{
                    {"*", "2", "3", "1"},
                    {"3", "4", "4", "*"},
                    {"2", "4", "*", "3"},
                    {"1", "*", "2", "1"}
            };

            for (int i = 0; i < firstWords.get(size).length(); i++) {
                isNext = true;
                int amount = 0;
                for (int rows = 0; rows < key.length; rows++) {
                    if (isNext) {
                        for (int column = 0; column < key[rows].length; column++) {
                            if (key[rows][column].equals("*")) {
                                arr[rows][column] = String.valueOf(firstWords.get(size).charAt(i));
                                amount++;
                                //  System.out.println("R" + rows  + " C" + column + " A" + a.charAt(i));

                                if (i < firstWords.get(size).length() - 1) {
                                    i++;
                                    if (amount == 4) {
                                        amount = 0;
                                        i--;
                                    }
                                } else {
                                    isNext = false;
                                    break;
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
                key = rotateMatrix(key);
            }

            if (size == firstWords.size() - 1) {
                String letters = "abcdefghijklmnopqrstuvwxyz";
                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr[i].length; j++) {
                        if (isDigit(arr[i][j])) {
                            String replace = "";
                            int number = (int) (Math.random() * letters.length());
                            replace += letters.charAt(number);
                            arr[i][j] = replace;
                        }
                    }
                }
            }

            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    encryptText += arr[i][j];
                }
            }

        }
        return encryptText;
    }


    private String decryptGrid(String message) {
        ArrayList<String> firstWords = new ArrayList<>();
        int index = 0;
        String temp = "";
        while (index < message.length()) {
            temp += message.charAt(index);
            if (temp.length() == 16) {
                firstWords.add(temp);
                temp = "";
            }
            index++;
        }
        if (temp.length() != 0) {
            firstWords.add(temp);
        }

        String[][] arr = {
                {"1", "2", "3", "1"},
                {"3", "4", "4", "2"},
                {"2", "4", "4", "3"},
                {"1", "3", "2", "1"}
        };

        String[][] key = {
                {"*", "2", "3", "1"},
                {"3", "4", "4", "*"},
                {"2", "4", "*", "3"},
                {"1", "*", "2", "1"}
        };

        String decryptText = "";
        for (int size = 0; size < firstWords.size(); size++) {
            int symbol = 0;
            for (int row = 0; row < arr.length; row++) {
                for (int column = 0; column < arr[row].length; column++) {
                    arr[row][column] = String.valueOf(firstWords.get(size).charAt(symbol));
                    symbol++;
                }
            }

            boolean isExit = false;
            for(int turn = 0; turn < 4 ; turn++){
                for (int row = 0; row < key.length; row++) {
                    for (int column = 0; column < key[row].length; column++) {
                        if (key[row][column].equals("*")) {
                            if (arr[row][column].equals("")) {
                                isExit = true;
                                break;
                            }
                            decryptText += arr[row][column];
                            arr[row][column] = "";
                        }
                    }
                    if (isExit) {
                        break;
                    }
                }
                if(isExit){
                    break;
                }
                key = rotateMatrix(key);
            }
        }
        return decryptText;
    }

    private String encryptVig(String message, String key){
        message = message.toLowerCase();
        message = message.replaceAll(" ","");
        key = key.toLowerCase();
        key = key.replaceAll(" ","");

        char[] alphabet = new char[33];
        String firstKey = key;
        int code = 1072;
        for(int i = 0; i < alphabet.length; i++){
            alphabet[i] = (char) code;
            if(code == 1077){
                i++;
                alphabet[i] = (char) 1105;
            }
            code++;
        }

        Character[][] main = new Character[3][message.length()]; // заполняем сообщением
        for(int i = 0; i < message.length(); i++){
            main[0][i] = message.charAt(i);
        }

        for (int i = 0; i < message.length() ; i++) {
            for (int j = 0; j < key.length() ; j++) {  // заполняем ключами
                main[1][i] = key.charAt(j);
                if(i < message.length()-1){
                    i++;
                    if(j == key.length()-1){
                        i--;
                    }
                }
                else {
                    break;
                }
            }
            key = createNewKey(key, alphabet);
        }

        for(int i = 0; i < main[2].length;i++){
            main[2][i] = findSymbolEncrypt(main[0][i],main[1][i],alphabet);
        }

        String encryptText = "";
        for (int i = 0; i < main[2].length ; i++) {
            encryptText += main[2][i];
        }

//        String allKeys = "";
//        for (int i = 0; i < main[1].length ; i++) {
//            allKeys += main[1][i];
//        }
//
//        char[][] tables = new char[allKeys.length()+1][33];
//        for (int i = 0; i < tables[0].length ; i++) {
//            tables[0][i] = alphabet[i];
//        }
//
//        for(int i = 1; i < tables.length; i++){
//            tables[i][0] = allKeys.charAt(i-1);
//            for (int j = 1; j < tables[i].length; j++) {
//                tables[i][j] = findNextSymbol(tables[i][j-1],alphabet);
//            }
//        }
//
//        boolean isFind;
//        for (int i = 0; i < main[2].length ; i++) {
//            isFind = false;
//            char messageSymbol = main[0][i];
//            char keySymbol = main[1][i];
//            for (int j = 1; j < tables.length; j++) {
//                if(tables[j][0] == keySymbol){
//                    for (int k = 0; k < tables[j].length ; k++) {
//                        if(tables[0][k] == messageSymbol){
//                            main[2][i] = tables[j][k];
//                            isFind = true;
//                            break;
//                        }
//                    }
//                }
//                if(isFind){
//                    break;
//                }
//            }
//        }
//        String encryptText = "";
//        for (int i = 0; i < main[2].length ; i++) {
//            encryptText += main[2][i];
//        }

//        for (int i = 0; i < main.length; i++) {
//            for (int j = 0; j < main[i].length; j++) {
//                System.out.print(main[i][j] + "  ");
//            }
//            System.out.println();
//        }
//
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        for (int i = 0; i < tables.length; i++) {
//            for (int j = 0; j < tables[i].length; j++) {
//                System.out.print(tables[i][j] + "  ");
//            }
//            System.out.println();
//        }
        return encryptText;
    }

    private String decryptVig(String message, String key){
        message = message.toLowerCase();
        message = message.replaceAll(" ","");
        key = key.toLowerCase();
        key = key.replaceAll(" ","");

        char[] alphabet = new char[33];
        String firstKey = key;
        int code = 1072;
        for(int i = 0; i < alphabet.length; i++){
            alphabet[i] = (char) code;
            if(code == 1077){
                i++;
                alphabet[i] = (char) 1105;
            }
            code++;
        }

        Character[][] main = new Character[3][message.length()]; // заполняем сообщением
        for(int i = 0; i < message.length(); i++){
            main[2][i] = message.charAt(i);
        }

        for (int i = 0; i < message.length() ; i++) {
            for (int j = 0; j < key.length() ; j++) {  // заполняем ключами
                main[1][i] = key.charAt(j);
                if(i < message.length()-1){
                    i++;
                    if(j == key.length()-1){
                        i--;
                    }
                }
                else {
                    break;
                }
            }
            key = createNewKey(key, alphabet);
        }

        for (int i = 0; i < main[0].length ; i++) {
            main[0][i] = findSymbolDecrypt(main[2][i],main[1][i],alphabet);
        }

//        String allKeys = "";
//        for (int i = 0; i < main[1].length ; i++) {
//            allKeys += main[1][i];
//        }
//
//        char[][] tables = new char[allKeys.length()+1][33];
//        for (int i = 0; i < tables[0].length ; i++) {
//            tables[0][i] = alphabet[i];
//        }
//
//        for(int i = 1; i < tables.length; i++){
//            tables[i][0] = allKeys.charAt(i-1);
//            for (int j = 1; j < tables[i].length; j++) {
//                tables[i][j] = findNextSymbol(tables[i][j-1],alphabet);
//            }
//        }
//
//        boolean isFind;
//        for (int i = 0; i < main[0].length; i++) {
//            isFind = false;
//            char cipherSymbol = main[2][i];
//            char keySymbol = main[1][i];
//            for (int j = 0; j < tables.length ; j++) {
//                if(tables[j][0] == keySymbol){
//                    for (int k = 0; k < tables[j].length ; k++) {
//                        if(tables[j][k] == cipherSymbol){
//                            main[0][i] = tables[0][k];
//                            isFind = true;
//                            break;
//                        }
//                    }
//                }
//                if(isFind){
//                    break;
//                }
//            }
//        }

        String decryptText = "";

        for (int i = 0; i < main[0].length; i++) {
            decryptText += main[0][i];
        }

//        for (int i = 0; i < main.length; i++) {
//            for (int j = 0; j < main[i].length; j++) {
//                System.out.print(main[i][j] + "  ");
//            }
//            System.out.println();
//        }
//
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        for (int i = 0; i < tables.length; i++) {
//            for (int j = 0; j < tables[i].length; j++) {
//                System.out.print(tables[i][j] + "  ");
//            }
//            System.out.println();
//        }
        return decryptText;
    }
    private boolean isDigit(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Character findSymbolEncrypt(char message, char key, char[] alphabet){
        Integer indexMessage = null;
        Integer indexKey = null;
        Integer indexEncrypt= null;
        for(int i = 0; i < alphabet.length; i++){
            if(alphabet[i] == message){
                 indexMessage = i;
            }
            if(alphabet[i] == key){
                indexKey = i;
            }
        }
        if(indexMessage != null && indexKey != null) {
            indexEncrypt = (indexMessage + indexKey) % alphabet.length;
            return alphabet[indexEncrypt];
        }
        return null;
    }

    private Character findSymbolDecrypt(char crypt, char key, char[] alphabet){
        Integer indexCrypt = null;
        Integer indexKey = null;
        Integer indexDecrypt= null;
        for(int i = 0; i < alphabet.length; i++){
            if(alphabet[i] == crypt){
                indexCrypt = i;
            }
            if(alphabet[i] == key){
                indexKey = i;
            }
        }
        if(indexCrypt != null && indexKey != null) {
            indexDecrypt = (indexCrypt + alphabet.length - indexKey) % alphabet.length;
            return alphabet[indexDecrypt];
        }
        return null;
    }


    private String[][] rotateMatrix(String[][] matrix) {
        String[][] result = new String[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[j][matrix.length - i - 1] = matrix[i][j];
            }
        }
        return result;
    }

    private String createNewKey(String key, char[] alphabet){
        String newString = key;
        for (int i = 0; i < key.length() ; i++) {
            for (int j = 0; j < alphabet.length; j++) {
                if(key.charAt(i) == alphabet[j]){
                    if(key.charAt(i) == 'я'){
                        newString = newString.substring(0,i)+'а'+newString.substring(i+1);
                    }
                    else {
                        char symbol = alphabet[j+1];
                        newString = newString.substring(0,i)+symbol+newString.substring(i+1);
                    }
                    break;
                }
            }
        }
        return newString;
    }

    private char findNextSymbol(char symbol, char[] alphabet){
        char newChar = '0';
        for (int i = 0; i < alphabet.length; i++) {
            if(symbol == alphabet[i]){
                if(symbol == 'я'){
                    newChar = 'а';
                }
                else {
                    newChar = alphabet[i+1];
                }
            }
        }
        return newChar;
    }

    private void writeFile(String text){
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(file, false);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.flush();
            fileWriter.close();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void someMessage(String str){
        try {
            Message message = new Message(str);
            message.display();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}