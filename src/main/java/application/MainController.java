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
    private Button Button_load_model, Button_set_contract, Button_execute, button_default_model, Button_update_model, Button_rule_list, Button_getRule, Button_getVariable, Button_messageState;

    @FXML
    private TextField loaded_model_path, Text_messageID_query, Text_input_query, updated_model_path,
            TextField_contract_address, Textfield_messageID_get, Textfield_variable_name, Textfield_variable_results,
            TextField_monitor_results;

    @FXML
    private TextArea Text_area;

    @FXML
    private ProgressIndicator progress;

    @FXML
    private ChoiceBox<String> Choice_variable_type;

    @FXML
    private VBox boxID;


    Alert a = new Alert(AlertType.NONE);
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
            Text_area.setText(finalText);
            a.show();

        } catch (Exception gpm) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore in getPastMessages:" + gpm);
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
                    finalText += "MessageID: " + set.getKey().get(i) + "With rule: \n" + set.getValue().get(i) + "\n";
                    Text_area.setText(finalText);
                    //Textfield_variable_results.setText(finalText);
                }
                String newLine = System.getProperty("line.separator");

            }

            String p = "caa\n" + "ddd";
            a.show();

        } catch (Exception gpu) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore in getPastUpdate:" + gpu);
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
                System.out.println("Addr: \n");
                System.out.println(addr);

                //System.out.println(address);
                // openContract(event, address);
            }
        } catch (Exception loadM) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Inserire un contratto valido.");
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
                this.boxID.setDisable(true);
                this.progress.setVisible(true);
                Translator t = new Translator();
                t.readModel(selectedFile);
                String finalRule = t.flowNodeSearch();
                t.createFile(selectedFile.getName(), finalRule);
                t.updateRules(u.getContractAddress());
                //Textfield_variable_results.setText("New contract deployed at: " + address);
                //System.out.println(updated_model_path.getText());
                this.boxID.setDisable(false);
                this.progress.setVisible(false);
            }

        } catch (Exception updateM) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore assicurarsi di:\n" + "1-Aver inserito un contratto valido\n" +
                    "2 - Aver caricato un contratto precedentemente con Load Custom Model\n");
            alert.showAndWait();
        }
    }

    public void button_default_model(ActionEvent event) {
        //LOAD DEFAULT MODEL
        try {
            this.a.setContentText("You selected Load Default Model");
            this.a.setAlertType(AlertType.CONFIRMATION);
            this.a.show();
        } catch (Exception bdm) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore nel caricamento del contratto in button_default_model(): \n" + bdm);
            alert.showAndWait();
        }
    }

    public void openContract(ActionEvent event) throws Exception {
        String os = getOperatingSystem();
        try {
            // ProcessTemplate processTemplate = u.deployContract(); //(funge)
            //Translator t = new Translator();
            //String address = t.deployAndUpload();
            BlockchainUtils u = new BlockchainUtils();
            String address = this.TextField_contract_address.getText();
            ProcessTemplate contract = this.u.loadContract(addr);
            // this.Text_area.setText("Contract loaded at: " + contract.getContractAddress());
            // ProcessTemplate processTemplate = u.loadContract(contract.getContractAddress()); //dentro ci va il contract generato dalla load
            //0x2fee6725a43e8fabf3706692c98a11312f079699
            //u.loadContract(address);
            /* Translator t = new Translator();
            String address = t.deployAndUpload();
            System.out.println(address);
            */

            if ((contract.getContractAddress() == null || address == null)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Warning");
                alert.setHeaderText("Smart contract address is null");
                alert.showAndWait();
            } else {
                //TODO vedere se con Linux stampa la distro oppure da solo Linux
                if (os.equals("Linux")) {
                    if (Runtime.getRuntime().exec(new String[]{"which", "xdg-open"}).getInputStream().read() != -1) {
                        Runtime.getRuntime().exec(new String[]{"xdg-open", "https://rinkeby.etherscan.io/address/" + contract.getContractAddress()});
                    } else {
                        System.out.println("Errore su openContract()\n");
                    }
                } else
                    Desktop.getDesktop().browse(new URI("https://rinkeby.etherscan.io/address/" + contract.getContractAddress()));
                //Desktop.getDesktop().browse(new URI("https://rinkeby.etherscan.io/address/" + address2));
                //Desktop.getDesktop().browse(new URI("https://rinkeby.etherscan.io/address/" + address));
            }

        } catch (Exception addr) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("First upload a model to obtain smart contract's addresss");
            alert.showAndWait();
        }
    }

    private String getOperatingSystem() {
        String os = System.getProperty("os.name");
        // System.out.println("Using System Property: " + os);
        return os;
    }

    public void ruleList(ActionEvent event) throws Exception {
        //LOAD DEFAULT MODEL
        try {
            this.a.setContentText("You selected GetMessage List");
            this.a.setAlertType(AlertType.CONFIRMATION);
            this.a.show();

            List idList = u.getIDs();
            String result = "";
            for (Object id : idList) {
                result += " " + id + ",";
            }

            if (result == null || result.isEmpty()) {
                System.out.println("Guarda che result e' null (o vuota)!!!\n");
            } else {
                System.out.println("Ecco la lista dei messaggi: " + result);
                this.Text_area.setText(result);
                //Textfield_variable_results.setText(result);
            }
        } catch (Exception ruleL) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore nella ruleList" + ruleL);
            alert.showAndWait();
        }
    }

    public void executeQuery(ActionEvent event) throws Exception {
        try {
            if (Text_messageID_query.getText() == null || Text_messageID_query.getText().isEmpty()) {
                System.out.println("\nAssicurati che il campo messageID sia valido\n");
            }

            if (Text_input_query.getText() == null || Text_input_query.getText().isEmpty()) {
                System.out.println("\nAssicurati che il campo input list sia valido\n");
            }

            boolean found = false;
            List idList = u.getIDs();
            for (Object id : idList) {
                if (Text_messageID_query.getText().equals(id)) {
                    System.out.println("Corrispondenza trovata: \n" + id);
                    found = true;
                    break;
                }
            }
            if (found == false) {
                System.out.println("Corrispondenza non trovata\n");
                throw new Exception("MessageID non valido");
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore nella executeQuery" + execQ);
            alert.showAndWait();
        }
    }

    public void setContract(ActionEvent event) {
        //TextField_contract_address.setText("0x4041d79f597a341d760d1c250cc6835d0b30ab3d1893214801adc1eb39a4738e");
        try {
            String address = this.TextField_contract_address.getText();
            // Qui gestiamo il caso in cui andiamo a mettere direttamente l'indirizzo nella barra
            //if (addr == null || addr.isEmpty()) {

            //    System.out.println("QUI CI ENTRIIIIIIIIIIIII??????");
            //System.out.println(addr);
            //    System.out.println(address);
            //if (addr != address) {
            //}
            System.out.println("Address: ");
            System.out.println(address);
            System.out.println("Addr: ");
            System.out.println(addr);
            // Se la barra e' nulla

            if (address != null && (address.isEmpty() == false)) {
                /*Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Inserire un address.");
                alert.showAndWait();*/
                addr = address;
            }
            ProcessTemplate contract = this.u.loadContract(addr);
            //System.out.println(contract);

            if (contract.getContractAddress() != null) {
                this.a.setContentText("You selected SetContract Address query: " + addr);
                this.a.setAlertType(AlertType.CONFIRMATION);
                this.Text_area.setText("Contract loaded at: " + contract.getContractAddress());
                this.a.show();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Attenzione");
                alert.setHeaderText("Inserire un address.");
                alert.showAndWait();
            }
        } catch (Exception setC) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Settare un indirizzo valido.");
            alert.showAndWait();
        }
    }

    public void getRule(ActionEvent event) throws Exception {
        try {
            this.a.setContentText("You selected getRule: " + this.Textfield_messageID_get.getText());
            this.a.setAlertType(AlertType.CONFIRMATION);

            String messageId = this.Textfield_messageID_get.getText();
            String rule = this.u.getRule(messageId);
            this.Text_area.setText(rule);
            this.a.show();
        } catch (Exception getR) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore nel getRule" + getR);
            alert.showAndWait();
        }
    }


    public void getVariable(ActionEvent event) throws Exception {

        try {
            this.a.setContentText("You selected getRule: " + this.Textfield_variable_name.getText() + "-->" + this.Choice_variable_type.getSelectionModel().getSelectedItem());
            this.a.setAlertType(AlertType.CONFIRMATION);
            this.a.show();
            String type = (String) this.Choice_variable_type.getSelectionModel().getSelectedItem();
            String varName = this.Textfield_variable_name.getText();
            String result = "";

            if (type.equals("String")) {
                result = this.u.getStringFromContract(varName);
            } else if (type.equals("Integer")) {
                result = String.valueOf(this.u.getIntFromContract(varName));
            } else if (type.equals("Boolean")) {
                result = String.valueOf(this.u.getBoolFromContract(varName));
            }

            if (result == null || result.isEmpty()) {
                System.out.println("\nAssicurati di: ");
                System.out.println("\n1 - Aver scelto un valore tra String, Integer e Boolean");
                System.out.println("\n2 - Aver inserito un valore valido nel campo\n");
            } else {
                this.Text_area.setText(result);
            }

        } catch (Exception getV) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore nel getVariable" + getV);
            alert.showAndWait();
        }

       /*
        if(result == null || result.isEmpty()) {
            System.out.println("Non hai settato result\n");
        }

        else {
            System.out.println("E' stato inserito un valore\n");
            this.Text_area.setText(result);
		    //this.Textfield_variable_results.setText(result);
        }*/
    }

    public void getMessageState(ActionEvent event) throws Exception {
        try {
            this.a.setContentText("You selected getRule: get message name");
            this.a.setAlertType(AlertType.CONFIRMATION);
            this.a.show();

            String messageId = this.Textfield_messageID_get.getText();
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
            //this.Textfield_variable_results.setText(state);

            this.Text_area.setText(state);
        } catch (Exception getMS) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Attenzione");
            alert.setHeaderText("Errore nel getMessageState" + getMS);
            alert.showAndWait();
        }
    }


    @FXML
    public void initializeChoice() {
        this.Choice_variable_type.getItems().addAll(FXCollections.observableArrayList("String", "Integer", "Boolean"));
        this.Choice_variable_type.setValue("Select a Variable Type");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        initializeChoice();

    }

}
