package in.regres.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.regres.pojo.UserDto;

import java.io.FileInputStream;
import java.io.IOException;

public class UserDataFactory {

    public static UserDto createDefaultUser() throws IOException {

        // Responsible for get JSON values and populate the object through DTO
        ObjectMapper objectMapper = new ObjectMapper();

        UserDto user = objectMapper.readValue(
                new FileInputStream ("src/test/resources/requestBody/postUser.json"),
                UserDto.class);

        return user;
    }

    public static UserDto createAValidUser () throws IOException {
        UserDto validUser = createDefaultUser();

        return validUser;
    }

    public static UserDto createAnUnnamedUser () throws IOException {
        UserDto unnamedUser = createDefaultUser();
        unnamedUser.setName("");

        return unnamedUser;
    }

    public static UserDto createAnUserToGet() throws IOException {

        UserDto user = createDefaultUser();

        return user;
    }

}
