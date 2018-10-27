package valber.com.br.movies.service.impl;

import com.google.gson.*;
import valber.com.br.movies.domain.Popular;

import java.lang.reflect.Type;

public class JsonConverterMovies implements JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonElement popular =    json.getAsJsonObject();
        if (json.getAsJsonObject() != null) {
            popular = json.getAsJsonObject();
        }

        return (new Gson().fromJson(popular, Popular.class));
    }
}
