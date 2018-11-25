package app.domain;
import java.io.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;


public final class JSONSerialize {
    public static void serializeToJson(Room room, String path)
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(path), room);
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
        }
        catch (JsonGenerationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Room deserializeFromJson(String path) {
        Room room = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            room = objectMapper.readValue(new File(path), Room.class);
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
        }
        catch (JsonGenerationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return room;
    }
}

