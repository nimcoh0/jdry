package org.softauto.analyzer.core.dto;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.softauto.analyzer.directivs.argument.Argument;
import org.softauto.analyzer.directivs.request.Request;
import org.softauto.analyzer.directivs.result.Result;
import org.softauto.analyzer.model.data.Data;
import org.softauto.analyzer.core.utils.CheckNotNullOrEmpty;
import org.softauto.analyzer.core.utils.Utils;

import java.util.LinkedList;

public class DataConverter extends Converter<DtoData, Data>{

    private static Logger logger = LogManager.getLogger(DataConverter.class);

    public DataConverter() {
        super(DataConverter::convertToData);
    }

    static String scenarioId;

    public DataConverter setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
        return this;
    }

    private static Result buildResult(DtoData dtoData){
        Result result = new Result();
        try {
            result.setType(dtoData.returnType);
            String value = dtoData.result;
            //Expression expression = ExpressionBuilder.newBuilder().setType(dtoData.returnType).setValue(dtoData.result).build().getExpression();
           // Expression expression = ExpressionBuilder.newBuilder().setType(dtoData.returnType).setDisplayType(DisplayType.STRING).setValue(dtoData.result).build().getExpression();
            if(value != null && Utils.isJson(value.toString())){
                value ="\""+Utils.javaEscape(value.toString())+"\"";
            }else if(value != null && result.getType().equals("java.lang.String")){
                value = "\""+value.toString()+"\"";
            }
            Object v  = CheckNotNullOrEmpty.set(value).setType(result.getType()).isNotNull().isNotEmpty().get();
            result.addValue(v);
        } catch (Exception e) {
            logger.error("fail to build result "+dtoData.getMethod(),e);
        }
        return result;
    }

    private static Request buildRequest(DtoData dtoData){
        LinkedList<Argument> arguments = new LinkedList<>();
        if(dtoData.getArgs() != null && dtoData.getArgs().size() > 0) {
            for (int i = 0; i < dtoData.getArgs().size(); i++) {
                try {
                    Argument argument = new Argument();

                    //Expression expression = ExpressionBuilder.newBuilder().setType(dtoData.getTypes().get(i)).setDisplayType(DisplayType.STRING).setValue(dtoData.getArgs().get(i)).build().getExpression();
                    //Expression expression = ExpressionBuilder.newBuilder().setType(dtoData.getTypes().get(i)).setValue(dtoData.getArgs().get(i)).build().getExpression();
                    Object value = dtoData.getArgs().get(i);
                    if(dtoData.getArgs().get(i) != null && Utils.isJson(dtoData.getArgs().get(i).toString())){
                        value = "\""+Utils.javaEscape(value.toString())+"\"";
                    }else if(value != null && dtoData.getTypes().get(i).equals("java.lang.String")){
                        value = "\""+value.toString()+"\"";
                    }
                    value = CheckNotNullOrEmpty.set(value).setType(dtoData.getTypes().get(i)).isNotNull().isNotEmpty().get();
                    argument.addValue(value);
                    argument.setType(dtoData.getTypes().get(i));
                    argument.setName(dtoData.getNames() != null && dtoData.getNames().size() > 0 ? dtoData.getNames().get(i) : null);
                    arguments.add(argument);
                } catch (Exception e) {
                   logger.error("fail build request ",e);
                }
            }
        }
        Request request = new Request();
        request.setArguments(arguments);
        return request;
    }

    private static LinkedList<Data> convertToData(LinkedList<DtoData> dtos) {
        LinkedList<Data> dataList = new LinkedList<>();

            for(DtoData dtoData : dtos){
               Data data = new Data();
                try {
                    data.setResponse(buildResult(dtoData));
                    data.setRequest(buildRequest(dtoData));
                    data.setPluginData(dtoData.getPluginData());
                    data.setTime(dtoData.getTime());
                    data.setPlugin(dtoData.getPlugin());
                    data.setThread(dtoData.getThread());
                    data.setClazz(dtoData.getClazz());
                    data.setMethod(dtoData.getMethod());
                    data.setId(dtoData.getId());
                    data.setScenarioId(scenarioId);
                    data.setThreadLocal(dtoData.getThreadLocal());
                } catch (Exception ex) {
                    logger.error("fail convert data "+dtoData.getMethod(),ex );
                }
                dataList.add(data);
            }
        logger.debug("successfully convert data . data list size "+dataList.size());
        return dataList;
    }
}
