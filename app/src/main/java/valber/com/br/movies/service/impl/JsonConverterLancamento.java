package valber.com.br.movies.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import valber.com.br.movies.domain.Lancamentos;

public class JsonConverterLancamento implements JsonDeserializer<Object> {

        private Object classe;

@Override
public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonElement popular =    json.getAsJsonObject();
        if (json.getAsJsonObject() != null) {
        popular = json.getAsJsonObject();
        }

        return (new Gson().fromJson(popular, Lancamentos.class));
        }
        }

