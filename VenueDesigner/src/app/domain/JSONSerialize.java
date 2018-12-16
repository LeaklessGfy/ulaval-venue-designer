package app.domain;

import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;

final class JSONSerialize {
    static void serializeToJson(Room room, String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(path), room);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static Room deserializeFromJson(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(path), Room.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

