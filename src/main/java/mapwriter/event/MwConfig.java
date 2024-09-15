package mapwriter.event;

import java.io.File;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.util.JsonUtils;
import mapwriter.MwUtil;

public class MwConfig {
    private final File file;

    private JsonObject jsonObject = new JsonObject();

    public MwConfig(File file) {
        this.file = file;
    }

    public void load() {
        if (!this.file.exists()) {
            return;// it will save and create file after first launch
        }
        JsonElement jsonElement = JsonUtils.parseJsonFile(this.file);
        if (jsonElement != null && jsonElement.isJsonObject()) {
            this.jsonObject = jsonElement.getAsJsonObject();
        }
    }

    public void save() {
        // before you save, you must fill the jsonObject first
        JsonUtils.writeJsonToFile(this.jsonObject, this.file);
    }

    JsonObject enterCategory(String category) {
        JsonElement jsonElement = JsonUtils.getNestedObject(this.jsonObject, category, true);
        if (jsonElement != null && jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        return null;
    }

    public int getInt(String category, String key, int defaultValue) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            if (JsonUtils.hasInteger(object, key)) {
                return object.get(key).getAsInt();
            }
        }
        return defaultValue;
    }

    public String getString(String category, String key, String defaultValue) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            if (JsonUtils.hasString(object, key)) {
                return object.get(key).getAsString();
            }
        }
        return defaultValue;
    }

    public int[] getIntArray(String category, String key, int[] defaultValue) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            if (JsonUtils.hasArray(object, key)) {
                JsonArray asJsonArray = object.get(key).getAsJsonArray();
                try {
                    int[] result = new int[asJsonArray.size()];
                    for (int i = 0; i < asJsonArray.size(); i++) {
                        result[i] = asJsonArray.get(i).getAsInt();
                    }
                    return result;
                } catch (Exception ignored) {
                }
            }
        }
        return defaultValue;
    }


    public boolean getOrSetBoolean(String category, String key, boolean defaultValue) {
        return this.getInt(category, key, defaultValue ? 1 : 0) != 0;
    }

    public void setBoolean(String category, String key, boolean value) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            object.add(key, new JsonPrimitive(value));
        }
    }

    public int getOrSetInt(String category, String key, int defaultValue, int minValue, int maxValue) {
        int value = this.getInt(category, key, defaultValue);
        return Math.min(Math.max(minValue, value), maxValue);
    }

    public void setInt(String category, String key, int value) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            object.add(key, new JsonPrimitive(value));
        }
    }

    public void setString(String category, String key, String value) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            object.add(key, new JsonPrimitive(value));
        }
    }

    public boolean hasKey(String category, String key) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            return object.has(key);
        }
        return false;
    }

    public boolean hasCategory(String category) {
        return this.enterCategory(category) != null;
    }

    public long getColour(String category, String key) {
        long value = -1;
        if (this.hasKey(category, key)) {
            try {
                String valueString = this.getString(category, key, "");
                if (valueString.length() > 0) {
                    value = Long.parseLong(valueString, 16);
                    value &= 0xffffffffL;
                }
            } catch (NumberFormatException e) {
                MwUtil.log("error: could not read colour from config file %s:%s", category, key);
                value = -1;
            }
        }
        return value;
    }

    public int getColour(String category, String key, int value) {
        long valueLong = this.getColour(category, key);
        if (valueLong >= 0L) {
            value = (int) (valueLong & 0xffffffffL);
        }
        return value;
    }

    public int getOrSetColour(String category, String key, int value) {
        long valueLong = this.getColour(category, key);
        if (valueLong >= 0L) {
            value = (int) (valueLong & 0xffffffffL);
        } else {
            this.setColour(category, key, value);
        }
        return value;
    }

    public void setColour(String category, String key, int n) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            object.add(key, new JsonPrimitive(String.format("%08x", n)));
        }
    }

//    public void setColour(String category, String key, int n, String comment) {
//        this.get(category, key, "00000000", comment).set(String.format("%08x", n));
//    }
//
//    public String getSingleWord(String category, String key) {
//        String value = "";
//        if (this.hasKey(category, key)) {
//            value = this.get(category, key, value).getString().trim();
//            int firstSpace = value.indexOf(' ');
//            if (firstSpace >= 0) {
//                value = value.substring(0, firstSpace);
//            }
//        }
//        return value;
//    }
//
//    public void setSingleWord(String category, String key, String value, String comment) {
//        if ((comment != null) && (comment.length() > 0)) {
//            value = value + " # " + comment;
//        }
//        this.get(category, key, value).set(value);
//    }

    public void getIntList(String category, String key, List<Integer> list) {
        // convert List of integers to integer array to pass as default value
        int size = list.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = list.get(i);
        }

        // get integer array from config file
        int[] arrayFromConfig = null;
        try {
            arrayFromConfig = this.getIntArray(category, key, array);
        } catch (Exception e) {
            e.printStackTrace();
            arrayFromConfig = null;
        }
        if (arrayFromConfig != null) {
            array = arrayFromConfig;
        }

        // convert integer array back to List of integers
        list.clear();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
    }

    public void setIntList(String category, String key, List<Integer> list) {
        JsonObject object = this.enterCategory(category);
        if (object != null) {
            JsonArray jsonArray = new JsonArray();
            list.forEach(x -> jsonArray.add(new JsonPrimitive(x)));
            object.add(key, jsonArray);
        }
    }
}
