package br.com.zenon.transaction.ingestor;

import br.com.zenon.transaction.domain.TransactionType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class PaySimTransactionValidator{
    private final String[] headers;

    public PaySimTransactionValidator(String headers){
        this.headers = headers.split(",", -1);
    };

    public void assertValidLine(String[] values){


        for(int i = 0; i < values.length; i++){
            isNotEmpty(this.headers[i], values[i]);
        }

        isPositive(headers[0], Integer.parseInt(values[0]));
        isValidEnum(headers[1], values[1], TransactionType.class);
        isGreaterOrEquals(headers[2], new BigDecimal(values[2]), new BigDecimal("0.00"));
        isGreaterOrEquals(headers[4], new BigDecimal(values[4]), new BigDecimal("0.00"));
        isGreaterOrEquals(headers[5], new BigDecimal(values[5]), new BigDecimal("0.00"));
        isGreaterOrEquals(headers[7], new BigDecimal(values[7]), new BigDecimal("0.00"));
        isGreaterOrEquals(headers[8], new BigDecimal(values[8]), new BigDecimal("0.00"));
        isIntegerIn(headers[9], Integer.parseInt(values[9]), List.of(0,1));
        isIntegerIn(headers[10], Integer.parseInt(values[10]), List.of(0,1));
    }


    private static <T extends Enum<T> >void isValidEnum(String field, String value, Class<T> clazz){
        try {
            Enum.valueOf(clazz, value);
        }catch (Exception e){
            throw new IllegalArgumentException(String.format("%s should be one of them %s : %s", field, Arrays.toString(clazz.getEnumConstants()), value));
        }
    }

    private  static void isNotEmpty(String field, String value){
        if(value.isBlank()){
            throw new IllegalArgumentException(String.format("%s should not be empty : %s", field, value));
        }
    }

    private static  void isPositive(String field, Number value){
        if(value.longValue() <= 0){
            throw new IllegalArgumentException(String.format("%s should be positive : %s", field, value));
        }
    }

    private static void isIntegerIn(String field, Integer value, List<Integer> list ){
        if(!list.contains(value)){
            throw new IllegalArgumentException(String.format("%s should be in %s: %s", field, list, value));
        }
    }

    private static void isGreaterOrEquals(String field, BigDecimal value, BigDecimal reference){
        if(value.compareTo(reference) <0 ){
            throw new IllegalArgumentException(String.format("%s should greater than or equals %s: %s", field, reference, value));
        }
    }
}