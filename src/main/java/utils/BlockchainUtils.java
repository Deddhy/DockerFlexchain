package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Ethereum.ProcessMonitor;
import Ethereum.ProcessTemplate;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainIdLong;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

public class BlockchainUtils {

    String projectPath = "D:\\Repos_git\\cleanmaven\\src\\main\\java\\org\\example";

    Web3j web3 = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/7de7b769074749c8a01386bd7808b003"));
    Admin adm = Admin.build(new HttpService("https://rinkeby.infura.io/v3/7de7b769074749c8a01386bd7808b003"));


    Credentials credentials = Credentials.create("df0c7d8b8e2eb0657d750307ffadf20dcdc47d075302163b3e58fe2cfbef6d9f");
    ProcessTemplate contract;
    BigInteger GAS_PRICE = BigInteger.valueOf(1_000_000_000L);
    BigInteger GAS_LIMIT = BigInteger.valueOf(15_000_000L);
    ContractGasProvider c = new DefaultGasProvider();
    TransactionManager m = new RawTransactionManager(web3, credentials, ChainIdLong.RINKEBY);

    private List<String> messageInputs = new ArrayList<>();

    //Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");


    public void wrapper(String fileName) throws Exception {
        String path = projectPath + File.separator + "ethereum" + File.separator;
        String abiPath = path + parseName(fileName, ".abi");
        String binPath = path + parseName(fileName, ".bin");

        String[] args2 = {"-a", abiPath, "-b", binPath, "-o", projectPath + File.separator, "-p",
                "ethereum",};

        SolidityFunctionWrapperGenerator.main(args2);
    }

    public void compile(String fileName) throws Exception {
        String fin = parseName(fileName, ".sol");
        String solPath = projectPath + File.separator + "ethereum" + File.separator + fin;
        String destinationPath = projectPath + File.separator + "ethereum";
        String[] comm = {"solc", solPath, "--bin", "--abi", "--overwrite", "-o", destinationPath};
        Runtime rt = Runtime.getRuntime();
        java.lang.Process p = rt.exec(comm);
        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String line;
        while ((line = bri.readLine()) != null) {
            System.out.println(line);
        }
        bri.close();
        while ((line = bre.readLine()) != null) {
            System.out.println(line);
        }
        bre.close();
        p.waitFor();
    }

    // 0x62A18a87Ba55FB2c26bBB18ccD07F9847e24C29d

    public ProcessMonitor loadMonitor() {
        TransactionManager m = new RawTransactionManager(web3, credentials, ChainIdLong.RINKEBY);
        ContractGasProvider c = new DefaultGasProvider();
        return ProcessMonitor.load("0x62A18a87Ba55FB2c26bBB18ccD07F9847e24C29d",
                web3,
                m,
                c);
    }

    public ProcessTemplate deployContract() throws Exception {
        /*Ethereum.ProcessMonitor monitor = loadMonitor();
        TransactionReceipt receipt = monitor.instantiateProcess().send();
        List<Ethereum.ProcessMonitor.NewContractEventResponse> response = monitor.getNewContractEvents(receipt);
        return response.get(0).newContract;*/
        //TransactionManager m = new RawTransactionManager(web3, credentials, ChainIdLong.RINKEBY);
        //System.out.println(c.getGasPrice());
        //System.out.println(c.getGasLimit());
        //ContractGasProvider c = new DefaultGasProvider();
        return ProcessTemplate.deploy(web3, m, c).send();
    }


    public static String parseName(String name, String extension) {
        String[] oldName = name.split("\\.");
        String newName = oldName[0] + extension;
        return newName;
    }

    public ProcessTemplate loadContract(String address) {
        //TransactionManager m = new RawTransactionManager(web3, credentials, ChainIdLong.RINKEBY);
        //BigInteger GAS_PRICE = BigInteger.valueOf(18_000_000_000L);
        //BigInteger GAS_LIMIT = BigInteger.valueOf(9_000_000L);
        //ContractGasProvider c = new StaticGasProvider(GAS_LIMIT, GAS_PRICE);
        //System.out.println(c.getGasPrice());
        //System.out.println(c.getGasLimit());
        return contract = ProcessTemplate.load(address, web3, m, c);
        /*return contract = Ethereum.ProcessTemplate.load(
                "0xe60c566C37A7DF703CDE71E81837232513836ee8",
                web3,
                credentials,
                GAS_PRICE,
                GAS_LIMIT);*/
    }

    public BigInteger getLatestBlockNumber() throws Exception {
        return web3.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getNumber();
    }


    public String getStringFromContract(String variable) throws Exception {
        String result = contract.getString(variable).send();
        return result;
    }

    public BigInteger getIntFromContract(String variable) throws Exception {
        BigInteger result = contract.getInt(variable).send();
        return result;
    }

    public Boolean getBoolFromContract(String variable) throws Exception {
        Boolean result = contract.getBool(variable).send();
        return result;
    }

    public BigInteger getState(String variable) throws Exception {
        BigInteger state = contract.getMessage(variable).send();
        return state;
    }

    public List getIDs() throws Exception {
        return contract.getIDs().send();
    }

    public String getContractAddress() {
        return contract.getContractAddress();
    }
    /*public void setMessageToContract(String messageId) throws Exception {
        contract.setMessage(messageId).send();
    }*/

    public void addRuleToContract(List<String> messageId, List<String> rule) throws Exception {
        contract.addRules(messageId, rule).send();
    }

    public void setVarialesToContract(List<String> types, List<String> variables, List<String> values, String messageId) throws Exception {
        //contract.setVariables(stringVar, stringVal, uintVar, uintVal, boolVar, boolVal).send();
        contract.setVariables(types, variables, values, messageId).send();
    }

    public void setRulesToContract(List<String> elementId, List<String> ruleToAdd) throws Exception {
        contract.setRules(elementId, ruleToAdd).send();
    }

    public List<String> getMessageInputs() {
        return messageInputs;
    }

    public void setMessageInputs(List<String> stringList) {
        this.messageInputs = stringList;
    }

    public String getSingleInput(int index) {
        return messageInputs.get(index);
    }

    public String getRule(String messageID) throws Exception {
        return contract.getRule(messageID).send();
    }

    public void deleteRule(List<String> id) throws Exception {
        contract.deleteRules(id).send();
    }

    public void executeMessage(String messageId, List<String> inputs) throws Exception {
        contract.executeMessage(messageId, inputs).send();
    }

    public HashMap<String, List<String>> pastRules() throws Exception {
        //TODO
        HashMap<String, List<String>> pastMessages = new HashMap<>();
        contract.messageExecuteEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).
                subscribe((eventResponse) -> {
                    String messageId = eventResponse.messageId;
                    ArrayList inputs = (ArrayList) eventResponse.inputs;
                    String from = eventResponse.log.getAddress();

                    System.out.println("message id: " + messageId);
                    System.out.println("message inputs: " + inputs);
                    System.out.println("message from: " + from);
                    System.out.println(pastMessages.size());
                    List<String> stringList = new ArrayList<>();
                    for (int i = 0; i < inputs.size(); i++) {
                        byte[] byteValue = ((Utf8String) inputs.get(i)).getValue().getBytes(StandardCharsets.UTF_8);
                        String stringValue = new String(byteValue, StandardCharsets.UTF_8);
                        stringList.add(stringValue);
                    }
                    pastMessages.put(messageId, stringList);
                });
        return pastMessages;
    }

    public HashMap<List<String>, List<String>> pastUpdates() {
        HashMap<List<String>, List<String>> pastRules = new HashMap<>();

        contract.newRuleEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).
                subscribe((eventResponse) -> {
                    ArrayList messageId = (ArrayList) eventResponse.messageId;
                    ArrayList rules = (ArrayList) eventResponse.rule;
                    String from = eventResponse.log.getAddress();
                    System.out.println("message ids: " + messageId.toString());
                    System.out.println("message inputs: " + rules.toString());
                    System.out.println("message from: " + from);

                    List<String> messageList = new ArrayList<>();
                    List<String> ruleList = new ArrayList<>();

                    for (int i = 0; i < messageId.size(); i++) {
                        byte[] byteValue = ((Utf8String) messageId.get(i)).getValue().getBytes(StandardCharsets.UTF_8);
                        String stringValue = new String(byteValue, StandardCharsets.UTF_8);
                        messageList.add(stringValue);
                    }
                    for (int i = 0; i < rules.size(); i++) {
                        byte[] byteValue = ((Utf8String) rules.get(i)).getValue().getBytes(StandardCharsets.UTF_8);
                        String stringValue = new String(byteValue, StandardCharsets.UTF_8);
                        ruleList.add(stringValue);
                    }
                    pastRules.put(messageList, ruleList);
                });
        return pastRules;
    }
}

