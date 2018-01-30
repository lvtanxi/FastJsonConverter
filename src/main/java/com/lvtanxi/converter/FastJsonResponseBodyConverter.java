package com.lvtanxi.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Collection;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Date: 2017-12-22
 * Time: 16:20
 * Description:
 */
final class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private Type mType;

    private ParserConfig config;
    private int featureValues;
    private Feature[] features;
    private Class<?> mBasicsType;

    FastJsonResponseBodyConverter(Type type, ParserConfig config, int featureValues, Class<?> basicsType, Feature... features) {
        mType = type;
        this.config = config;
        this.featureValues = featureValues;
        this.features = features;
        this.mBasicsType = basicsType;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        if (mType == null)
            throw new RuntimeException("FastJsonResponseBodyConverter :泛型不能为空!");
        try {
            String body = responseBody.string();
            if (isEmptyBody(body))
                return null;

            if (mBasicsType !=null)
                return (T) obtainValue(body);

            return JSON.parseObject(body, mType, config, featureValues, features != null ? features : EMPTY_SERIALIZER_FEATURES);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            responseBody.close();
        }
    }

    private boolean isEmptyBody(String body) {
        return null == body || body.isEmpty() || "null".equals(body);
    }


    private Object obtainValue(String body) {
        try {
            Constructor<?> constructor = mBasicsType.getConstructor(String.class);
            return constructor.newInstance(body);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }



    private boolean isCoolection(Class<?> clzaa) {
        if (clzaa == null)
            return false;
        if (clzaa.isAssignableFrom(Collection.class))
            return true;
        boolean flag;
        for (Class<?> aClass : clzaa.getInterfaces()) {
            flag = isCoolection(aClass);
            if (flag)
                return true;
        }
        return false;
    }
}
