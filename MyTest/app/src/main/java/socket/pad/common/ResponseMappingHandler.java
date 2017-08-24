package socket.pad.common;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ResponseMappingHandler<Type extends Response> {

    private static final String TAG = "ResponseMappingHandler";
    private static final Gson gson = new Gson();
    private final Class<Type> parseClass;

    public ResponseMappingHandler(Class<Type> paramClass) {
        this.parseClass = paramClass;
    }

    private Type exceptionObjectMappingCase(String paramString, boolean paramBoolean) {
        try {
            Type localResponse = this.parseClass.newInstance();
            //            localResponse.setResult(makeExceptionResult(paramString, paramBoolean));
            return localResponse;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    private Type mappingJsonToObject(String response, boolean paramBoolean) {
        try {
            JsonElement resElement = gson.fromJson(response, JsonElement.class);
            Type localResponse = gson.fromJson(resElement, this.parseClass);
            return localResponse;
        } catch (Exception localException) {
            Log.e(TAG, "Error occur while mapping response to object");
            localException.printStackTrace();
        }
        return exceptionObjectMappingCase(response, paramBoolean);
    }

    public Type onSuccess(String paramString) {
        Type localResponse = mappingJsonToObject(paramString, true);
        return localResponse;
    }
}
