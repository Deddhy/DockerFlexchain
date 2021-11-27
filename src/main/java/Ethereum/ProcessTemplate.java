package Ethereum;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
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
import java.util.concurrent.Callable;

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
public class ProcessTemplate extends Contract {
    public static final String BINARY = "60806040526000600660006101000a81548160ff021916908360ff1602179055503480156200002d57600080fd5b5060007348c49978328758080f2fb8aba79205ef97bf806e90508073ffffffffffffffffffffffffffffffffffffffff166338aba755306040518263ffffffff1660e01b8152600401620000829190620000ce565b600060405180830381600087803b1580156200009d57600080fd5b505af1158015620000b2573d6000803e3d6000fd5b50505050506200011f565b620000c881620000eb565b82525050565b6000602082019050620000e56000830184620000bd565b92915050565b6000620000f882620000ff565b9050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b611dd5806200012f6000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c80639c981fcb116100715780639c981fcb1461019d578063d55d113d146101cd578063d7e730af146101eb578063e96f8ba51461021b578063f9d946e314610237578063fda69fae14610253576100b4565b80630cc4e8d8146100b95780631bd95155146100e95780635e94422a14610119578063705ffad5146101355780637172b127146101515780639535ce121461016d575b600080fd5b6100d360048036038101906100ce9190611691565b610283565b6040516100e09190611922565b60405180910390f35b61010360048036038101906100fe9190611691565b6102b8565b6040516101109190611996565b60405180910390f35b610133600480360381019061012e9190611521565b61037e565b005b61014f600480360381019061014a9190611562565b610609565b005b61016b600480360381019061016691906115ce565b610815565b005b61018760048036038101906101829190611691565b610c67565b6040516101949190611907565b60405180910390f35b6101b760048036038101906101b29190611691565b610c9c565b6040516101c4919061193d565b60405180910390f35b6101d5610d4c565b6040516101e291906118ae565b60405180910390f35b61020560048036038101906102009190611691565b610e25565b604051610212919061193d565b60405180910390f35b610235600480360381019061023091906116d2565b610ed5565b005b610251600480360381019061024c9190611562565b610f7f565b005b61026d60048036038101906102689190611691565b61117f565b60405161027a9190611996565b60405180910390f35b60006002826040516102959190611897565b908152602001604051809103902060009054906101000a900460ff169050919050565b6000808290506000809250600090505b815181101561037757600082828151811061030c577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b602001015160f81c60f81b60f81c905060308160ff1610158015610334575060398160ff1611155b15610363576030816103469190611b54565b60ff16600a856103569190611afa565b6103609190611aa4565b93505b50808061036f90611c75565b9150506102c8565b5050919050565b60005b81518110156106055760018282815181106103c5577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516040516103da9190611897565b908152602001604051809103902060006103f491906112a2565b60005b6000805490508110156105f15761051a60008281548110610441577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b90600052602060002001805461045690611c12565b80601f016020809104026020016040519081016040528092919081815260200182805461048290611c12565b80156104cf5780601f106104a4576101008083540402835291602001916104cf565b820191906000526020600020905b8154815290600101906020018083116104b257829003601f168201915b505050505084848151811061050d577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516111a7565b156105de5760008181548110610559577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b90600052602060002001600061056f91906112a2565b60028383815181106105aa577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516040516105bf9190611897565b908152602001604051809103902060006101000a81549060ff02191690555b80806105e990611c75565b9150506103f7565b5080806105fd90611c75565b915050610381565b5050565b6000600660009054906101000a900460ff1660ff16141561065857816000908051906020019061063a9291906112e2565b506001600660006101000a81548160ff021916908360ff1602179055505b60005b82518110156107d75781818151811061069d577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b602002602001015160018483815181106106e0577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516040516106f59190611897565b90815260200160405180910390209080519060200190610716929190611342565b5060016002848381518110610754577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516040516107699190611897565b908152602001604051809103902060006101000a81548160ff021916908360028111156107bf577f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b021790555080806107cf90611c75565b91505061065b565b507fcdef059bd14f2473119cbb7cf2c5da69826d8c126b9390534b5f46b06db74c8982826040516108099291906118d0565b60405180910390a15050565b600084511115610bf55760005b8451811015610bf3576108aa858281518110610867577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516040518060400160405280600681526020017f737472696e6700000000000000000000000000000000000000000000000000008152506111a7565b15610967578281815181106108e8577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b6020026020010151600385838151811061092b577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516040516109409190611897565b90815260200160405180910390209080519060200190610961929190611342565b50610be0565b6109e68582815181106109a3577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516040518060400160405280600481526020017f75696e74000000000000000000000000000000000000000000000000000000008152506111a7565b15610a9b57610a34838281518110610a27577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516102b8565b6005858381518110610a6f577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b6020026020010151604051610a849190611897565b908152602001604051809103902081905550610bdf565b610b1a858281518110610ad7577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516040518060400160405280600481526020017f626f6f6c000000000000000000000000000000000000000000000000000000008152506111a7565b15610bde57610b68838281518110610b5b577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b6020026020010151611200565b6004858381518110610ba3577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b6020026020010151604051610bb89190611897565b908152602001604051809103902060006101000a81548160ff0219169083151502179055505b5b5b8080610beb90611c75565b915050610822565b505b60028082604051610c069190611897565b908152602001604051809103902060006101000a81548160ff02191690836002811115610c5c577f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b021790555050505050565b6000600482604051610c799190611897565b908152602001604051809103902060009054906101000a900460ff169050919050565b6060600382604051610cae9190611897565b90815260200160405180910390208054610cc790611c12565b80601f0160208091040260200160405190810160405280929190818152602001828054610cf390611c12565b8015610d405780601f10610d1557610100808354040283529160200191610d40565b820191906000526020600020905b815481529060010190602001808311610d2357829003601f168201915b50505050509050919050565b60606000805480602002602001604051908101604052809291908181526020016000905b82821015610e1c578382906000526020600020018054610d8f90611c12565b80601f0160208091040260200160405190810160405280929190818152602001828054610dbb90611c12565b8015610e085780601f10610ddd57610100808354040283529160200191610e08565b820191906000526020600020905b815481529060010190602001808311610deb57829003601f168201915b505050505081526020019060010190610d70565b50505050905090565b6060600182604051610e379190611897565b90815260200160405180910390208054610e5090611c12565b80601f0160208091040260200160405190810160405280929190818152602001828054610e7c90611c12565b8015610ec95780601f10610e9e57610100808354040283529160200191610ec9565b820191906000526020600020905b815481529060010190602001808311610eac57829003601f168201915b50505050509050919050565b6000600283604051610ee79190611897565b908152602001604051809103902060006101000a81548160ff02191690836002811115610f3d577f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b02179055507f3be6db30a3513d60905e653d96c44308a41ff4ad025e6874321674c5b1857af18282604051610f7392919061195f565b60405180910390a15050565b60005b825181101561117a57818181518110610fc4577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101516001848381518110611007577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b602002602001015160405161101c9190611897565b9081526020016040518091039020908051906020019061103d929190611342565b506000838281518110611079577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b60200260200101519080600181540180825580915050600190039060005260206000200160009091909190915090805190602001906110b9929190611342565b50600160028483815181106110f7577f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b602002602001015160405161110c9190611897565b908152602001604051809103902060006101000a81548160ff02191690836002811115611162577f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b0217905550808061117290611c75565b915050610f82565b505050565b60006005826040516111919190611897565b9081526020016040518091039020549050919050565b6000816040516020016111ba919061193d565b60405160208183030381529060405280519060200120836040516020016111e1919061193d565b6040516020818303038152906040528051906020012014905092915050565b6000611241826040518060400160405280600481526020017f74727565000000000000000000000000000000000000000000000000000000008152506111a7565b1561124f576001905061129d565b61128e826040518060400160405280600581526020017f66616c73650000000000000000000000000000000000000000000000000000008152506111a7565b1561129c576000905061129d565b5b919050565b5080546112ae90611c12565b6000825580601f106112c057506112df565b601f0160209004906000526020600020908101906112de91906113c8565b5b50565b828054828255906000526020600020908101928215611331579160200282015b82811115611330578251829080519060200190611320929190611342565b5091602001919060010190611302565b5b50905061133e91906113e5565b5090565b82805461134e90611c12565b90600052602060002090601f01602090048101928261137057600085556113b7565b82601f1061138957805160ff19168380011785556113b7565b828001600101855582156113b7579182015b828111156113b657825182559160200191906001019061139b565b5b5090506113c491906113c8565b5090565b5b808211156113e15760008160009055506001016113c9565b5090565b5b8082111561140557600081816113fc91906112a2565b506001016113e6565b5090565b600061141c611417846119d6565b6119b1565b9050808382526020820190508285602086028201111561143b57600080fd5b60005b8581101561148557813567ffffffffffffffff81111561145d57600080fd5b80860161146a89826114f7565b8552602085019450602084019350505060018101905061143e565b5050509392505050565b60006114a261149d84611a02565b6119b1565b9050828152602081018484840111156114ba57600080fd5b6114c5848285611bd0565b509392505050565b600082601f8301126114de57600080fd5b81356114ee848260208601611409565b91505092915050565b600082601f83011261150857600080fd5b813561151884826020860161148f565b91505092915050565b60006020828403121561153357600080fd5b600082013567ffffffffffffffff81111561154d57600080fd5b611559848285016114cd565b91505092915050565b6000806040838503121561157557600080fd5b600083013567ffffffffffffffff81111561158f57600080fd5b61159b858286016114cd565b925050602083013567ffffffffffffffff8111156115b857600080fd5b6115c4858286016114cd565b9150509250929050565b600080600080608085870312156115e457600080fd5b600085013567ffffffffffffffff8111156115fe57600080fd5b61160a878288016114cd565b945050602085013567ffffffffffffffff81111561162757600080fd5b611633878288016114cd565b935050604085013567ffffffffffffffff81111561165057600080fd5b61165c878288016114cd565b925050606085013567ffffffffffffffff81111561167957600080fd5b611685878288016114f7565b91505092959194509250565b6000602082840312156116a357600080fd5b600082013567ffffffffffffffff8111156116bd57600080fd5b6116c9848285016114f7565b91505092915050565b600080604083850312156116e557600080fd5b600083013567ffffffffffffffff8111156116ff57600080fd5b61170b858286016114f7565b925050602083013567ffffffffffffffff81111561172857600080fd5b611734858286016114cd565b9150509250929050565b600061174a83836117e5565b905092915050565b600061175d82611a43565b6117678185611a66565b93508360208202850161177985611a33565b8060005b858110156117b55784840389528151611796858261173e565b94506117a183611a59565b925060208a0199505060018101905061177d565b50829750879550505050505092915050565b6117d081611b88565b82525050565b6117df81611bbe565b82525050565b60006117f082611a4e565b6117fa8185611a77565b935061180a818560208601611bdf565b61181381611d7a565b840191505092915050565b600061182982611a4e565b6118338185611a88565b9350611843818560208601611bdf565b61184c81611d7a565b840191505092915050565b600061186282611a4e565b61186c8185611a99565b935061187c818560208601611bdf565b80840191505092915050565b61189181611ba7565b82525050565b60006118a38284611857565b915081905092915050565b600060208201905081810360008301526118c88184611752565b905092915050565b600060408201905081810360008301526118ea8185611752565b905081810360208301526118fe8184611752565b90509392505050565b600060208201905061191c60008301846117c7565b92915050565b600060208201905061193760008301846117d6565b92915050565b60006020820190508181036000830152611957818461181e565b905092915050565b60006040820190508181036000830152611979818561181e565b9050818103602083015261198d8184611752565b90509392505050565b60006020820190506119ab6000830184611888565b92915050565b60006119bb6119cc565b90506119c78282611c44565b919050565b6000604051905090565b600067ffffffffffffffff8211156119f1576119f0611d4b565b5b602082029050602081019050919050565b600067ffffffffffffffff821115611a1d57611a1c611d4b565b5b611a2682611d7a565b9050602081019050919050565b6000819050602082019050919050565b600081519050919050565b600081519050919050565b6000602082019050919050565b600082825260208201905092915050565b600082825260208201905092915050565b600082825260208201905092915050565b600081905092915050565b6000611aaf82611ba7565b9150611aba83611ba7565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff03821115611aef57611aee611cbe565b5b828201905092915050565b6000611b0582611ba7565b9150611b1083611ba7565b9250817fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0483118215151615611b4957611b48611cbe565b5b828202905092915050565b6000611b5f82611bb1565b9150611b6a83611bb1565b925082821015611b7d57611b7c611cbe565b5b828203905092915050565b60008115159050919050565b6000819050611ba282611d8b565b919050565b6000819050919050565b600060ff82169050919050565b6000611bc982611b94565b9050919050565b82818337600083830152505050565b60005b83811015611bfd578082015181840152602081019050611be2565b83811115611c0c576000848401525b50505050565b60006002820490506001821680611c2a57607f821691505b60208210811415611c3e57611c3d611d1c565b5b50919050565b611c4d82611d7a565b810181811067ffffffffffffffff82111715611c6c57611c6b611d4b565b5b80604052505050565b6000611c8082611ba7565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff821415611cb357611cb2611cbe565b5b600182019050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6000601f19601f8301169050919050565b60038110611d9c57611d9b611ced565b5b5056fea26469706673582212209b7cfdf53e20fdc6ed92257c203f2b1405595b1f01eea2bf1f992070b83c9bf564736f6c63430008040033";

    public static final String FUNC_ADDRULES = "addRules";

    public static final String FUNC_DELETERULES = "deleteRules";

    public static final String FUNC_EXECUTEMESSAGE = "executeMessage";

    public static final String FUNC_GETBOOL = "getBool";

    public static final String FUNC_GETIDS = "getIDs";

    public static final String FUNC_GETINT = "getInt";

    public static final String FUNC_GETMESSAGE = "getMessage";

    public static final String FUNC_GETRULE = "getRule";

    public static final String FUNC_GETSTRING = "getString";

    public static final String FUNC_SETRULES = "setRules";

    public static final String FUNC_SETVARIABLES = "setVariables";

    public static final String FUNC_STRINGTOUINT = "stringToUint";

    public static final Event MESSAGEEXECUTE_EVENT = new Event("messageExecute", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
    ;

    public static final Event NEWRULE_EVENT = new Event("newRule", 
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
    ;

    @Deprecated
    protected ProcessTemplate(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ProcessTemplate(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ProcessTemplate(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ProcessTemplate(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<MessageExecuteEventResponse> getMessageExecuteEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(MESSAGEEXECUTE_EVENT, transactionReceipt);
        ArrayList<MessageExecuteEventResponse> responses = new ArrayList<MessageExecuteEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            MessageExecuteEventResponse typedResponse = new MessageExecuteEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.messageId = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.inputs = (List<String>) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<MessageExecuteEventResponse> messageExecuteEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, MessageExecuteEventResponse>() {
            @Override
            public MessageExecuteEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(MESSAGEEXECUTE_EVENT, log);
                MessageExecuteEventResponse typedResponse = new MessageExecuteEventResponse();
                typedResponse.log = log;
                typedResponse.messageId = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.inputs = (List<String>) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<MessageExecuteEventResponse> messageExecuteEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MESSAGEEXECUTE_EVENT));
        return messageExecuteEventFlowable(filter);
    }

    public List<NewRuleEventResponse> getNewRuleEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(NEWRULE_EVENT, transactionReceipt);
        ArrayList<NewRuleEventResponse> responses = new ArrayList<NewRuleEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            NewRuleEventResponse typedResponse = new NewRuleEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.messageId = (List<String>) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.rule = (List<String>) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewRuleEventResponse> newRuleEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewRuleEventResponse>() {
            @Override
            public NewRuleEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(NEWRULE_EVENT, log);
                NewRuleEventResponse typedResponse = new NewRuleEventResponse();
                typedResponse.log = log;
                typedResponse.messageId = (List<String>) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.rule = (List<String>) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<NewRuleEventResponse> newRuleEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWRULE_EVENT));
        return newRuleEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addRules(List<String> messageIds, List<String> newRrules) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDRULES, 
                Arrays.<Type>asList(new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(messageIds, Utf8String.class)),
                new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(newRrules, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> deleteRules(List<String> ids) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DELETERULES, 
                Arrays.<Type>asList(new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(ids, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> executeMessage(String messageToExecute, List<String> inputs) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXECUTEMESSAGE, 
                Arrays.<Type>asList(new Utf8String(messageToExecute),
                new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(inputs, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> getBool(String variable) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBOOL, 
                Arrays.<Type>asList(new Utf8String(variable)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<List> getIDs() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETIDS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getInt(String variable) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETINT, 
                Arrays.<Type>asList(new Utf8String(variable)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getMessage(String messageId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETMESSAGE, 
                Arrays.<Type>asList(new Utf8String(messageId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getRule(String messageId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETRULE, 
                Arrays.<Type>asList(new Utf8String(messageId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> getString(String variable) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETSTRING, 
                Arrays.<Type>asList(new Utf8String(variable)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setRules(List<String> messageId, List<String> rule) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETRULES, 
                Arrays.<Type>asList(new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(messageId, Utf8String.class)),
                new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(rule, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setVariables(List<String> types, List<String> variables, List<String> values, String messageID) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETVARIABLES, 
                Arrays.<Type>asList(new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(types, Utf8String.class)),
                new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(variables, Utf8String.class)),
                new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(values, Utf8String.class)),
                new Utf8String(messageID)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> stringToUint(String s) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_STRINGTOUINT, 
                Arrays.<Type>asList(new Utf8String(s)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static ProcessTemplate load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProcessTemplate(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ProcessTemplate load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ProcessTemplate(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ProcessTemplate load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ProcessTemplate(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ProcessTemplate load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ProcessTemplate(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ProcessTemplate> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ProcessTemplate.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ProcessTemplate> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ProcessTemplate.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ProcessTemplate> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ProcessTemplate.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ProcessTemplate> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ProcessTemplate.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class MessageExecuteEventResponse extends BaseEventResponse {
        public String messageId;

        public List<String> inputs;
    }

    public static class NewRuleEventResponse extends BaseEventResponse {
        public List<String> messageId;

        public List<String> rule;
    }
}
