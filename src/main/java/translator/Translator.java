package translator;

import Ethereum.ProcessTemplate;
import javafx.scene.control.Alert;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.impl.instance.ModelElementInstanceImpl;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import utils.BlockchainUtils;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Translator {
    BpmnModelInstance modelInstance;
    List<String> participants;
    List<String> participantsWithoutDuplicates;
    String orCondition = "";
    List<String> rulesList = new ArrayList<>();
    List<String> idList = new ArrayList<>();

    /*public static void main(String[] args) throws Exception {
        File f = new File("C:/Users/alkit/Desktop/Dottorato/Blockchain flexibility/case study/simple_travel.bpmn");
        Translator t = new Translator();
        t.readModel(f);
        String finalRule = t.flowNodeSearch();
        t.createFile(f.getName(), finalRule);
       // t.updateRules("0xa0b2Dca9F0bFe557707Cb863b44D4eAC1367C214");
        t.deployAndUpload();


    }*/

    public String deployAndUpload() throws Exception {
        BlockchainUtils u = new BlockchainUtils();
        // String address = u.deployContract();
        ProcessTemplate processTemplate = u.deployContract();
        String address = processTemplate.getContractAddress();
        //System.out.println("L'indirizzo dello smart contract è: " + "https://rinkeby.etherscan.io/address/" + address);
        u.loadContract(address);
        /*for(String id : idList){
            System.out.println("pushed id: " + id);
        }*/


        u.setRulesToContract(idList, rulesList);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Rules successfully uploaded in the smart contract");
        alert.showAndWait();
        //System.out.println("Rules successfully uploaded in the smart contract");

        return address;

    }

    public void executeMessage(String address, String messageId, List<String> inputs) throws Exception {
        BlockchainUtils u = new BlockchainUtils();
        u.loadContract(address);
        u.executeMessage(messageId, inputs);
    }

    public List<String> listOfNewEditedRules(String address) throws Exception {
        BlockchainUtils u = new BlockchainUtils();
        ProcessTemplate contract = u.loadContract(address);
        List<String> list = new ArrayList<>();
        List<String> finalRules = new ArrayList<>();
        List<String> finalIds = new ArrayList<>();
        List<String> newRules = new ArrayList<>();
        List<String> newIds = new ArrayList<>();
        List<String> oldIds = new ArrayList<>();

        List ids = contract.getIDs().send();//C2

        for (int i = 0; i < idList.size(); i++) {
            if (ids.contains(idList.get(i))) {
                String res = contract.getRule(idList.get(i)).send();
                if (res.equals(rulesList.get(i))) {
                    System.out.println("Same rule with id: " + idList.get(i));
                } else {
                    System.out.println("Rule to update: " + idList.get(i));
                    list.add(rulesList.get(i));
                }
            } else {
                System.out.println("Rule to add:  " + idList.get(i));
                list.add(rulesList.get(i));
            }
        }
        return list;
    }

    public void updateRules(String address) throws Exception {
        BlockchainUtils u = new BlockchainUtils();
        ProcessTemplate contract = u.loadContract(address);

        List<String> finalRules = new ArrayList<>();
        List<String> finalIds = new ArrayList<>();
        List<String> newRules = new ArrayList<>();
        List<String> newIds = new ArrayList<>();
        List<String> oldIds = new ArrayList<>();

        List ids = contract.getIDs().send();//C2

        for (int i = 0; i < idList.size(); i++) {
            if (ids.contains(idList.get(i))) {
                String res = contract.getRule(idList.get(i)).send();
                //If the rule is already present and is the same don't touch it
                if (res.equals(rulesList.get(i))) {
                    System.out.println("Same rule with id: " + idList.get(i));
                } else {
                    //if the rule is different but with same id buffer it and finally push to the setRules function
                    //that in this case serves as an update
                    System.out.println("Rule to update: " + idList.get(i));
                    finalIds.add(idList.get(i));
                    finalRules.add(rulesList.get(i));
                }
            } else {
                //if the rule is different and new then add it to the contract,
                System.out.println("Rule to add:  " + idList.get(i));
                newIds.add(idList.get(i));
                newRules.add(rulesList.get(i));
                //finalIds.add(idList.get(i));
                //finalRules.add(rulesList.get(i));
            }
        }
        for (Object t : ids) {
            if (!idList.contains((String) t)) {
                oldIds.add((String) t);
                System.out.println("Rule to remove with id: " + (String) t);
            }
        }
        if (!finalIds.isEmpty()) {
            u.setRulesToContract(finalIds, finalRules);
            System.out.println("Rules updated");
        }
        if (!newIds.isEmpty()) {
            u.addRuleToContract(newIds, newRules);
            System.out.println("Rules added");
        }
        if (!oldIds.isEmpty()) {
            u.deleteRule(oldIds);
            System.out.println("Rules removed");
        }

    }

    /*public void updateRules(String address) throws Exception {
        File f = new File("C:/Users/alkit/Downloads/pizzaUpdate.bpmn");
        translator.Translator t = new translator.Translator();
        t.readModel(f);
        String finalRule = t.FlowNodeSearch();
        t.createFile("buffer_" + f.getName(), finalRule);
        t.compareRules("pizza.bpmn.drl", "buffer_" + f.getName()+ ".drl");


        //BlockchainUtils u = new BlockchainUtils();
        //u.loadContract(address);
    }*/


    public void readModel(File file) {
        modelInstance = Bpmn.readModelFromFile(file);
    }

    public void getParticipants() {
        Collection<Participant> parti = modelInstance.getModelElementsByType(Participant.class);
        for (Participant p : parti) {
            participants.add(p.getName());
        }
        participantsWithoutDuplicates = new ArrayList<>(new HashSet<>(participants));
    }


    //HANDLE EVENT BASED GW
    public String flowNodeSearch() {
        String rule = "";
        //get all the sequence flow of the model
        for (SequenceFlow flow : modelInstance.getModelElementsByType(SequenceFlow.class)) {
            //get the target of the sequence flow, so the element to be processed
            ModelElementInstance node = modelInstance.getModelElementById(flow.getAttributeValue("targetRef"));
            ModelElementInstance source = modelInstance.getModelElementById(flow.getAttributeValue("sourceRef"));
            if (node instanceof ModelElementInstanceImpl && !(node instanceof EndEvent)
                    && !(node instanceof ParallelGateway) && !(node instanceof ExclusiveGateway)
                    && !(node instanceof EventBasedGateway)) {
                ChoreographyTask task = new ChoreographyTask((ModelElementInstanceImpl) node, modelInstance);
                String requestId = getRequestId(task);
                String responseId = getResponseId(task);
                //if the request exists check if it is enable and if the previous is completed

                if (!requestId.isEmpty()) {
                    try {
                        if (!(source instanceof StartEvent) && !(source instanceof ParallelGateway)) {
                            orCondition = ", ";
                        }
                        String singleRule = "";
                        //get the previous id

                        getPreviousId(flow);

                        //create the rule for the request
                        singleRule += "rule \'" + requestId + "\'\n" +
                                "when\n" +
                                "   b : BlockchainUtils(b.getState(\'" + requestId + "\')==0" + orCondition;
                        //if there is a gateway condition to check add it
                        if (!checkForCondition(flow).isEmpty())
                            singleRule += ", " + checkForCondition(flow) + ")\n";
                        else
                            singleRule += ")\n";
                        singleRule += "then\n" +
                                "" + createThenPart(getMessageName(requestId), requestId) + "\n " +
                                "end\n\n";
                        idList.add(requestId);
                        rulesList.add(singleRule);
                        rule += singleRule;
                    } catch (Exception e) {
                        System.out.println("fake message detected on the request: " + requestId);
                        System.out.println("exception: " + e);

                        rule += "";
                    }
                }
                //if the response exists, check if it is enabled and if the previous request is completed
                if (!responseId.isEmpty()) {
                    try {
                        String singleRule = "";
                        singleRule += "rule \'" + responseId + "\'\n" +
                                "when\n" +
                                "   b : BlockchainUtils(b.getState(\'" + responseId + "\')==0, b.getState(\'" + requestId + "\')==2)\n" +
                                "then\n" +
                                "" + createThenPart(getMessageName(responseId), responseId) + "\n " +
                                "end\n\n";
                        idList.add(responseId);
                        rulesList.add(singleRule);
                        rule += singleRule;
                    } catch (Exception e) {
                        System.out.println("fake message detected on the response: " + responseId);
                        rule += "";
                    }
                }

            }
        }

        return rule;

    }

    public String getRequestId(ChoreographyTask task) {

        if (task.getRequest() != null) {
            MessageFlow requestMessageFlowRef = task.getRequest();
            MessageFlow requestMessageFlow = modelInstance.getModelElementById(requestMessageFlowRef.getId());
            Message requestMessage = modelInstance
                    .getModelElementById(requestMessageFlow.getAttributeValue("messageRef"));
            return requestMessage.getAttributeValue("id");
        }
        return "";
    }


    public String getResponseId(ChoreographyTask task) {
        if (task.getResponse() != null) {
            MessageFlow responseMessageFlowRef = task.getResponse();
            MessageFlow responseMessageFlow = modelInstance.getModelElementById(responseMessageFlowRef.getId());
            Message responseMessage = modelInstance
                    .getModelElementById(responseMessageFlow.getAttributeValue("messageRef"));
            return responseMessage.getAttributeValue("id");
        }
        return "";
    }

    public String getMessageName(String id) {
        Message message = modelInstance.getModelElementById(id);
        if (!message.getAttributeValue("name").isEmpty()) {
            return message.getAttributeValue("name");
        }
        return "";

    }

    public String createThenPart(String messageName, String messageId) {

        //assuming message is in format name(x y, x1 y1, x2 y2)
        String replaced1 = messageName.replace(")", "");
        String[] split1 = replaced1.split("\\(");
        //now the list contains ["x y", "x1 y1", "x2 y2"];
        List<String> split2 = Arrays.asList(split1[1].split(","));

        String listTypes = "    List<String> types = Arrays.asList(new String[]{";
        String listNames = "    List<String> variables = Arrays.asList(new String[]{";
        String listInputs = "   List<String> values = Arrays.asList(new String[]{";

        for (String param : split2) {
            //now is ["x", "y"]
            String[] params = param.split(" ");
            List<String> buffer = new ArrayList<>();
            //check for removing white spaces that confuse the parser
            for (String c : params) {
                if (!c.isEmpty()) {
                    buffer.add(c);
                }
            }
            //check if the element is the last for the comma after the argument
            if (split2.indexOf(param) == (split2.size() - 1)) {
                listTypes += "\'" + buffer.get(0) + "\'";
            } else {
                listTypes += "\'" + buffer.get(0) + "\',";
            }
            //same structure but for creating the second list of param names
            if (split2.indexOf(param) == (split2.size() - 1)) {
                listNames += "\'" + buffer.get(1) + "\'";
            } else {
                listNames += "\'" + buffer.get(1) + "\',";
            }
            //if the element is the last one end otherwise add the comma
            if (split2.indexOf(param) == (split2.size() - 1)) {
                listInputs += "b.getSingleInput(" + split2.indexOf(param) + ")";
            } else {
                listInputs += "b.getSingleInput(" + split2.indexOf(param) + "),";
            }
        }
        listTypes += "});\n";
        listNames += "});\n";
        listInputs += "});\n";
        String setVariables = "b.setVarialesToContract(types, variables, values, \'" + messageId + "\');";

        return listTypes + listNames + listInputs + setVariables;

    }


    public String checkForCondition(SequenceFlow flow) {
        //recursive search for the latest previous message
        //if the element is a gw I get the source until a task appears
        //then with the task I get the response or request
        String getter = "";
        ModelElementInstance previous = modelInstance.getModelElementById(flow.getAttributeValue("sourceRef"));
        if (previous instanceof ExclusiveGateway && flow.getName() != null) {
            //condition in format uint x > 5 || string x == "a" || bool x == true
            String conditionToParse = flow.getName();
            String[] params = conditionToParse.split(" ");
            List<String> buffer = new ArrayList<>();
            //check for removing white spaces that confuse the parser
            for (String c : params) {
                if (!c.isEmpty()) {
                    buffer.add(c);
                }
            }
            //depending on the param type a different getter is created
            if (conditionToParse.contains("uint")) {
                getter += "b.getIntFromContract(\'" + buffer.get(1) + "\')" +
                        "" + buffer.get(2) + buffer.get(3);
            } else if (conditionToParse.contains("string")) {
                //System.out.println("buffer di stringhe: " + buffer);
                getter += "b.getStringFromContract(\'" + buffer.get(1) + "\')" +
                        "" + buffer.get(2) + buffer.get(3);
            } else if (conditionToParse.contains("bool")) {
                getter += "b.getBoolFromContract(\'" + buffer.get(1) + "\')" +
                        "" + buffer.get(2) + buffer.get(3);
            }
        }
        return getter;
    }

    public void getPreviousId(SequenceFlow flow) {
        List<String> previousIDs = new ArrayList<>();
        ModelElementInstance node = modelInstance.getModelElementById(flow.getAttributeValue("sourceRef"));
        //check previous if it is a message get its response or request
        if (node instanceof ModelElementInstanceImpl && !(node instanceof EndEvent)
                && !(node instanceof ParallelGateway) && !(node instanceof ExclusiveGateway)
                && !(node instanceof EventBasedGateway)) {
            ChoreographyTask task = new ChoreographyTask((ModelElementInstanceImpl) node, modelInstance);
            //check if the request or response is empty or is a fake message
            if (!getResponseId(task).isEmpty() && idList.contains(getResponseId(task))) {
                //System.out.println("previous is a response"+ getResponseId(task));
                orCondition += "b.getState(\'" + getResponseId(task) + "\')==2";
            } else if (!getRequestId(task).isEmpty() && idList.contains(getRequestId(task))) {
                //System.out.println("previous is a request"+ getRequestId(task));
                orCondition += "b.getState(\'" + getRequestId(task) + "\')==2";
            }
        }
        //if there is a gateway take the incomings flows
        // for each incoming take the source and call again the method
        else if (node instanceof ExclusiveGateway) {
            List<Object> inc = Arrays.asList(((ExclusiveGateway) node).getIncoming().toArray());
            for (Object f : inc) {
                //if the element is the latest remove the OR condition
                //cehck if the gw has more inputs so if the it is a join put the incoming msgs in OR
                getPreviousId((SequenceFlow) f);
                if (inc.indexOf(f) != (inc.size() - 1))
                    orCondition += " || ";
            }
        } else if (node instanceof ParallelGateway) {
            List<Object> inc = Arrays.asList(((ParallelGateway) node).getIncoming().toArray());
            for (Object f : inc) {
                //if the element is the latest remove the OR condition
                //cehck if the gw has more inputs so if it is a join put the incoming msgs in AND
                getPreviousId((SequenceFlow) f);
                if (inc.indexOf(f) != (inc.size() - 1))
                    orCondition += " && ";
            }
        } else if (node instanceof EventBasedGateway) {
            List<Object> inc = Arrays.asList(((EventBasedGateway) node).getIncoming().toArray());
            for (Object f : inc) {
                //if the element is the latest remove the OR condition
                //cehck if the gw has more inputs so if it is aN EVENT based put the incoming msgs in OR
                getPreviousId((SequenceFlow) f);
                if (inc.indexOf(f) != (inc.size() - 1))
                    orCondition += " || ";
            }
        }
    }

    public void createFile(String filename, String finalRule) throws IOException {
        //"src" + File.separator + "main"
        //                + File.separator + "resources" + File.separator +
/*        URL pathToDRL = getClass().getResource("drools");
        FileWriter wChor = new FileWriter(new File("src" + File.separator +"main" + File.separator +"resources" +
                File.separator + "drools" + File.separator + filename + ".drl"));
        BufferedWriter bChor = new BufferedWriter(wChor);
        String initial = "import java.util.List;\n" +
                "import java.util.Arrays;\n\n";
        bChor.write(initial+finalRule);
        bChor.flush();
        bChor.close();*/
    }

    /*public void compareRules(String oldFile, String newFile) throws FileNotFoundException {
        File oldF = new File(oldFile);
        File newF = new File(newFile);
        Scanner myReader1 = new Scanner(oldF);
        Scanner myReader3 = new Scanner(oldF);
        Scanner myReader2 = new Scanner(newF);

        ArrayList<String> listOld = new ArrayList<>();
        ArrayList<String> listNew = new ArrayList<>();

        String oldBuffer = "";
        String newBuffer = "";

        int add1 = 0;
        String buffer1 = "";
        while (myReader1.hasNextLine()) {
            String b = myReader1.nextLine();
            if(b.contains("rule") ){
                add1 = 1;
                buffer1 += b;
            } else if(add1 == 1 && !b.contains("end")){
                buffer1 += b;
            } else if(add1 == 1 && b.contains("end")){
                buffer1 += b;
                add1 = 0;
                listOld.add(buffer1);
                buffer1 = "";
            }
        }
        myReader1.close();

        int add2 = 0;
        String buffer2 = "";
        while (myReader2.hasNextLine()) {
            String b = myReader2.nextLine();
            if(b.contains("rule") ){
                add2 = 1;
                buffer2 += b;
            } else if(add2 == 1 && !b.contains("end")){
                buffer2 += b;
            } else if(add2 == 1 && b.contains("end")){
                buffer2 += b;
                add2 = 0;
                listNew.add(buffer2);
                buffer2 = "";
            }
        }
        myReader2.close();


        for (String s: listNew) {
            if(listOld.contains(s)){
                System.out.println("c'è già uguale");
            } else{
                System.out.println("da agiungere o modificare");
            }
        }
        for (String s: listOld) {
            if(!listNew.contains(s)){
                System.out.println("DA RIMUOVERE");
            }
        }


        List<String> modifyList = new ArrayList<>();
        String modifyBuffer = "";
        while (myReader2.hasNextLine()) {
            String b = myReader2.nextLine();
            newBuffer += b;
            int modify = 0;

            //it checks for an existing rule to modify
            if(b.contains("rule") && oldBuffer.contains(b)){
                System.out.println("da modificare " + b);
                modify = 1;
                modifyBuffer += b;
            } else if(b.contains("rule") && !oldBuffer.contains(b)){
                System.out.println("da aggiungere " + b);
            } else if(modify == 1 && !b.contains("end")){
                modifyBuffer+= b;
            } else if(modify == 1 && b.contains("end")){
                modifyBuffer+= b;
                modify = 0;
            }
        }
        myReader2.close();

        while(myReader3.hasNextLine()){
            String b = myReader3.nextLine();
            if(b.contains("rule") && !newBuffer.contains(b)){
                System.out.println("da rimuovere " + b);
            }
        }
        myReader3.close();
    } */

}
