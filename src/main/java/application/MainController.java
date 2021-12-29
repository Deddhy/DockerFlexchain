package application;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.List;

import javafx.scene.control.*;
import Ethereum.ProcessTemplate;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import translator.Translator;
import utils.BlockchainUtils;


public class MainController implements Initializable {

    @FXML
    private Button Button_set_contract,Button_getVariable, Button_rule_list, Button_getRule, Button_messageState, Button_load_model, Button_update_model, Button_execute, Visualize_Contract;

    @FXML
    private TextField loaded_model_path, Text_messageID_query, Text_input_query, updated_model_path,
            TextField_contract_address, Textfield_messageID_get, Text_variable, Textfield_variable_results,
            TextField_monitor_results;

    @FXML
    private TextArea Text_area, Text_areaTwo, Text_areaThree;

    @FXML
    private ChoiceBox<String> ChoiceBox_getVariable ;

    @FXML
    private TabPane boxID;

    Alert a = new Alert(AlertType.NONE);
    Alert alert = new Alert(AlertType.ERROR);
    BlockchainUtils u = new BlockchainUtils();

    private String addr;


    public void getPastMessages(ActionEvent event) throws Exception {
        try {
            a.setContentText("You selected contract:  " + u.getContractAddress());
            a.setAlertType(AlertType.CONFIRMATION);
            //String messageId = Textfield_messageID_get.getText();
            HashMap<String, List<String>> pastRules = u.pastRules();
            String finalText = " ";
            for (Map.Entry<String, List<String>> set : pastRules.entrySet()) {
                finalText += "Message sent with ID: " + set.getKey();
                finalText += " and with value: ";
                for (String singleValue : set.getValue()) {
                    finalText += singleValue + ", ";
                }
            }
            //0xa0b2Dca9F0bFe557707Cb863b44D4eAC1367C214
            Text_areaThree.setText(finalText);
            a.show();

        } catch (Exception gpm) {
            alert.setTitle("Warning");
            alert.setHeaderText("An error has occured\nPlease be sure to:\n1) Have uploaded a valid contract\n");
            alert.showAndWait();
        }
    }


    public void getPastUpdates(ActionEvent event) throws Exception {
        try {
            a.setContentText("You selected contract:  " + u.getContractAddress());
            a.setAlertType(AlertType.CONFIRMATION);
            //String messageId = Textfield_messageID_get.getText();
            HashMap<List<String>, List<String>> pastRules = u.pastUpdates();
            String finalText = " ";

            for (Map.Entry<List<String>, List<String>> set : pastRules.entrySet()) {
                for (int i = 0; i < set.getKey().size(); i++) {
                    finalText += "MessageID: " + set.getKey().get(i) + "\nWith rule: \n" + set.getValue().get(i) + "\n";
                    Text_areaThree.setText(finalText);
                }
                String newLine = System.getProperty("line.separator");
            }
            String p = "caa\n" + "ddd";
            a.show();
        } catch (Exception gpu) {
            alert.setTitle("Warning");
            alert.setHeaderText("An error has occured\nPlease be sure to:\n1) Have uploaded a valid contract\n");
            alert.showAndWait();
        }
    }

    public void loadModel(ActionEvent event) throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Files");
            //		fileChooser.setInitialDirectory(new File("CollaborationRepository/"));
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Choreography", "*.bpmn"),
                    new ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                this.loaded_model_path.setText(selectedFile.getName());
                Translator t = new Translator();
                t.readModel(selectedFile);
                String finalRule = t.flowNodeSearch();
                t.createFile(selectedFile.getName(), finalRule);
                String address = t.deployAndUpload();
                addr = address;
                ProcessTemplate contract = this.u.loadContract(addr);
                Text_area.setText("Address: " + addr);
            }
        } catch (Exception loadM) {
            alert.setTitle("Warning");
            alert.setHeaderText("Please insert a valid contract.");
            alert.showAndWait();
        }
    }

    public void updateModel(ActionEvent event) throws Exception {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Files");
            //fileChooser.setInitialDirectory(new File("CollaborationRepository/"));
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Choreography", "*.bpmn"),
                    new ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                this.updated_model_path.setText(selectedFile.getName());
                //this.boxID.setDisable(true);
                //this.progress.setVisible(true);
                //this.progress.progressProperty();
                Translator t = new Translator();
                t.readModel(selectedFile);
                String finalRule = t.flowNodeSearch();
                t.createFile(selectedFile.getName(), finalRule);
                List<String> list = new ArrayList<>();
                list = t.listOfNewEditedRules(u.getContractAddress());

                if (list.isEmpty() || list == null) {
                    a.setContentText("No rules to update");
                    a.setAlertType(AlertType.CONFIRMATION);
                    a.show();
                } else {

                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Updating Rules");
                    alert.setHeaderText("The following rules will be updated:");
                    alert.setContentText("For further information, please click on show details.");

                    TextArea area = new TextArea(list.toString());
                    area.setWrapText(true);
                    area.setEditable(false);

                    alert.getDialogPane().setExpandableContent(area);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        t.updateRules(u.getContractAddress());
                    } else {
                        a.setContentText("Update canceled.");
                        a.setAlertType(AlertType.CONFIRMATION);
                        a.show();
                    }
                }
            }

        } catch (Exception updateM) {
            alert.setTitle("Warning");
            alert.setHeaderText("Please verify that:\n" + "1 - A valid contract has been updated\n" +
                    "2 - A valid address has been provided in Set Contract Address box\n");
            alert.showAndWait();
            //this.boxID.setDisable(false);
            //this.progress.setVisible(false);
        }
    }

    public void button_default_model(ActionEvent event) {
        //LOAD DEFAULT MODEL
        try {
            this.a.setContentText("You selected Load Default Model");
            this.a.setAlertType(AlertType.CONFIRMATION);
            this.a.show();
        } catch (Exception bdm) {
            alert.setTitle("Warning");
            alert.setHeaderText("Uploading contract has encountered an error: \n" + bdm);
            alert.showAndWait();
        }
    }

    public void openContract(ActionEvent event) throws Exception {
        try {
            BlockchainUtils u = new BlockchainUtils();
            String address = this.TextField_contract_address.getText();
            ProcessTemplate contract = this.u.loadContract(addr);

            if ((contract.getContractAddress() == null || address == null)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Warning");
                alert.setHeaderText("Smart contract address is null");
                alert.showAndWait();
            } else {
                String url = "https://rinkeby.etherscan.io/address/" + contract.getContractAddress();
                String os = System.getProperty("os.name").toLowerCase();
                Runtime rt = Runtime.getRuntime();
                if (os.indexOf("win") >= 0) {
                    // this doesn't support showing urls in the form of "page.html#nameLink"
                    rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (os.indexOf("mac") >= 0) {
                    rt.exec("open " + url);
                } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
                    // Prova, in modo indipendente, a capire che unix è installato
                    // Prova di alcuni possibili browser esistenti
                    // Crea una stringa di comandi che sembrano "browser1 "url || "browser2 "url" || ..."
                    if (Runtime.getRuntime().exec(new String[]{"which", "xdg-open"}).getInputStream().read() != -1) {
                        Runtime.getRuntime().exec(new String[]{"xdg-open", "https://rinkeby.etherscan.io/address/" + contract.getContractAddress()});
                    } else {
                        System.out.println("Errore su openContract()\n");
                    }
                } else {
                    alert.setTitle("Warning");
                    alert.setHeaderText("An error has occured during the opening of the link");
                    alert.showAndWait();
                }
            }
        } catch (Exception addr) {
            alert.setTitle("Warning");
            alert.setHeaderText("First upload a model to obtain smart contract's addresss");
            alert.showAndWait();
        }
    }

    public void ruleList(ActionEvent event) throws Exception {
        //LOAD DEFAULT MODEL
        try {

            List idList = u.getIDs();
            String result = "";
            for (Object id : idList) {
                result += " " + id + ",";
            }

            if (result == null || result.isEmpty()) {
                System.out.println("result is null or empty\n");
            } else {
                System.out.println("Messages list: " + result);
                this.Text_areaThree.setText(result);
                //Textfield_variable_results.setText(result);
            }
        } catch (Exception ruleL) {
            alert.setTitle("Warning");
            alert.setHeaderText("An error has occured\nPlease be sure that: \n1) You set up a valid contract");
            alert.showAndWait();
        }
    }

    public void executeQuery(ActionEvent event) throws Exception {
        try {
            if (Text_messageID_query.getText() == null || Text_messageID_query.getText().isEmpty()) {
                throw new Exception();
            }

            if (Text_input_query.getText() == null || Text_input_query.getText().isEmpty()) {
                throw new Exception();
            }

            boolean found = false;
            List idList = u.getIDs();
            for (Object id : idList) {
                if (Text_messageID_query.getText().equals(id)) {
                    alert.setAlertType(AlertType.CONFIRMATION);
                    alert.setHeaderText("Matched: \n" + id);
                    alert.showAndWait();
                    found = true;
                    break;
                }
            }
            if (found == false) {
                throw new Exception();
            }

            String message = "You selected Execute query: " + Text_messageID_query.getText() + " -->" + Text_input_query.getText();
            String content = this.Text_input_query.getText();
            this.a.setContentText(message);
            this.a.setAlertType(AlertType.CONFIRMATION);
            this.a.show();

            String[] splitted = content.split(",");
            List<String> parameters = new ArrayList<>();
            for (String elem : splitted) {
                parameters.add(elem);
            }
            this.u.executeMessage(this.Text_messageID_query.getText(), parameters);
        } catch (Exception execQ) {
            alert.setTitle("Warning");
            alert.setHeaderText("An error has occured\nPlease check:\n1) You uploaded a valid contract\n2) The messageID and input field are filled correctly\n3) The message ID exists in the message list\n");
            alert.showAndWait();
        }
    }

    public void setContract(ActionEvent event) {
        //TextField_contract_address.setText("0x4041d79f597a341d760d1c250cc6835d0b30ab3d1893214801adc1eb39a4738e");
        try {
            String address = this.TextField_contract_address.getText();
            System.out.println("Address: ");
            System.out.println(address);
            System.out.println("Addr: ");
            System.out.println(addr);
            // Se la barra e' nulla

            if (address != null && (address.isEmpty() == false)) {
                addr = address;
                Button_update_model.setDisable(false);
            }
            ProcessTemplate contract = this.u.loadContract(addr);

            if (contract.getContractAddress() != null) {
                this.a.setContentText("You selected SetContract Address query: " + addr);
                this.a.setAlertType(AlertType.CONFIRMATION);
                this.Text_areaTwo.setText("Contract loaded at: " + contract.getContractAddress());
                this.a.show();
            } else {
                alert.setTitle("Warning");
                alert.setHeaderText("Insert an adress.");
                alert.showAndWait();
            }
        } catch (Exception setC) {
            alert.setTitle("Warning");
            alert.setHeaderText("Please set a valid address.");
            alert.showAndWait();
        }
    }

    public void getRule(ActionEvent event) throws Exception {
        try {
            String messageId = this.Textfield_messageID_get.getText();
            if (messageId == null || messageId.isEmpty()) {
                throw new Exception();
            } else {
                this.a.setContentText("You selected getRule: " + this.Textfield_messageID_get.getText());
                this.a.setAlertType(AlertType.CONFIRMATION);
            }
            String rule = this.u.getRule(messageId);
            this.Text_areaThree.setText(rule);
            this.a.show();
        } catch (Exception getR) {
            alert.setTitle("Warning");
            alert.setHeaderText("An error has occured\nPlease be sure that:\n1) You set up a valid contract\n2) The MessageID field is valid");
            alert.showAndWait();
        }
    }


    public void getVariable(ActionEvent event) throws Exception {

        try {

            String type = (String) this.ChoiceBox_getVariable.getSelectionModel().getSelectedItem();
            String varName = this.Text_variable.getText();
            String result = "";

            if (type.equals("String")) {
                result = this.u.getStringFromContract(varName);
            } else if (type.equals("Integer")) {
                result = String.valueOf(this.u.getIntFromContract(varName));
            } else if (type.equals("Boolean")) {
                result = String.valueOf(this.u.getBoolFromContract(varName));
            } else if (ChoiceBox_getVariable.getValue().equals("Select a Variable Type")) {
                throw new Exception();
            }
            if (this.Text_variable.getText() == null || this.Text_variable.getText().isEmpty()) {
                throw new Exception();
            } else {
                this.Text_areaThree.setText(result);
                this.a.setContentText("You selected getRule: " + this.Text_variable.getText() + "-->" + this.ChoiceBox_getVariable.getSelectionModel().getSelectedItem());
                this.a.setAlertType(AlertType.CONFIRMATION);
                this.a.show();
            }
            System.out.println("QUI ENTRO!");
            this.Text_area.setText(result);
            this.a.setAlertType(AlertType.CONFIRMATION);
            this.a.show();

        } catch (Exception getV) {
            alert.setTitle("Warning");
            alert.setHeaderText("An error has occured\nPlease be sure that:\n1) You uploaded a valid contract\n2) You selected the type\n3) You inserted the variable\n");
            alert.showAndWait();
        }
    }

    public void getMessageState(ActionEvent event) throws Exception {
        try {
            String messageId = this.Textfield_messageID_get.getText();
            if (messageId == null || messageId.isEmpty()) {
                throw new Exception();
            }

            this.a.setContentText("You selected getRule: get message name");
            this.a.setAlertType(AlertType.CONFIRMATION);
            this.a.show();

            BigInteger rule = this.u.getState(messageId);
            String state = "";
            System.out.println(rule.intValue());
            switch (rule.intValue()) {
                case 0:
                    state = "ENABLED";
                    break;
                case 1:
                    state = "DISABLED";
                    break;
                case 2:
                    state = "COMPLETED";
                    break;
            }
            System.out.println("State: " + state);
            this.Text_areaThree.setText(state);
        } catch (Exception getMS) {
            alert.setTitle("Warning");
            alert.setHeaderText("An error has occured\nPlease be sure to:\n1) Have uploaded a valid model\n2) The MessageID field is valid\n");
            alert.showAndWait();
        }
    }

    @FXML
    public void initializeChoice() {
        this.ChoiceBox_getVariable.getItems().addAll(FXCollections.observableArrayList("String", "Integer", "Boolean"));
        this.ChoiceBox_getVariable.setValue("Select a Variable Type");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeChoice();
    }
}
