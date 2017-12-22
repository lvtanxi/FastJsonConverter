package com.lvtanxi.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

    private Type mType;

    private ParserConfig config;
    private int featureValues;
    private Feature[] features;

    FastJsonResponseBodyConverter(Type type, ParserConfig config, int featureValues, Feature... features) {
        mType = type;
        this.config = config;
        this.featureValues = featureValues;
        this.features = features;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        try {
            String body = responseBody.string();
            if (isEmptyBody(body))
                body = isCoolection(mType.getClass()) ? "[]" : "{}";
            return JSON.parseObject(body, mType, config, featureValues, features != null ? features : EMPTY_SERIALIZER_FEATURES);
        } catch (Exception e) {
            throw new RuntimeException("数据解析错误!");
        } finally {
            responseBody.close();
        }
    }

    private boolean isEmptyBody(String body) {
        return null == body || body.isEmpty() || "null".equals(body);
    }

    private boolean isCoolection(Class<?> fc) {
        if (fc.isAssignableFrom(Collection.class))
            return true;
        boolean flag;
        for (Class<?> aClass : fc.getInterfaces()) {
            flag = isCoolection(aClass);
            if (flag)
                return true;
        }
        return false;
    }
}
