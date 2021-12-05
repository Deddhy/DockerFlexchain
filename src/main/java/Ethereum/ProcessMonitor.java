package Ethereum;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.8.4.
 */
@SuppressWarnings("rawtypes")
public class ProcessMonitor extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b5061016d806100206000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c806338aba75514610030575b600080fd5b61004a6004803603810190610045919061009b565b61004c565b005b7f6837ff1e738d95fc8bb5f12ce1513f42866f6c59c226c77342c4f36a1958ea108160405161007b91906100d3565b60405180910390a150565b60008135905061009581610120565b92915050565b6000602082840312156100ad57600080fd5b60006100bb84828501610086565b91505092915050565b6100cd816100ee565b82525050565b60006020820190506100e860008301846100c4565b92915050565b60006100f982610100565b9050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b610129816100ee565b811461013457600080fd5b5056fea26469706673582212203dcee6f2ef7c2f569248bd028dc06bd0ddaa65f301fd642f2af0627a6892aac964736f6c63430008040033";

    public static final String FUNC_INSTANTIATEPROCESS = "instantiateProcess";

    public static final Event NEWCONTRACT_EVENT = new Event("newContract",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
            }));
    ;

    @Deprecated
    protected ProcessMonitor(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ProcessMonitor(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ProcessMonitor(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ProcessMonitor(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<NewContractEventResponse> getNewContractEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWCONTRACT_EVENT, transactionReceipt);
        ArrayList<NewContractEventResponse> responses = new ArrayList<NewContractEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewContractEventResponse typedResponse = new NewContractEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewContractEventResponse> newContractEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewContractEventResponse>() {
            @Override
            public NewContractEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWCONTRACT_EVENT, log);
                NewContractEventResponse typedResponse = new NewContractEventResponse();
                typedResponse.log = log;
                typedResponse.newContract = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewContractEventResponse> newContractEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWCONTRACT_EVENT));
        return newContractEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> instantiateProcess(String newProcess) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_INSTANTIATEPROCESS,
                Arrays.<Type>asList(new Address(160, newProcess)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ProcessMonitor load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProcessMonitor(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ProcessMonitor load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProcessMonitor(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ProcessMonitor load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ProcessMonitor(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ProcessMonitor load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ProcessMonitor(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ProcessMonitor> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ProcessMonitor.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ProcessMonitor> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ProcessMonitor.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ProcessMonitor> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ProcessMonitor.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ProcessMonitor> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ProcessMonitor.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class NewContractEventResponse extends BaseEventResponse {
        public String newContract;
    }
}
