package in.regres.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.regres.pojo.UserDto;

import java.io.FileInputStream;
import java.io.IOException;

public class UserDataFactory {
    public static UserDto createUser() throws IOException {

        // Responsible for get JSON values and populate the object through DTO
        ObjectMapper objectMapper = new ObjectMapper();

        UserDto user = objectMapper.readValue(
                new FileInputStream ("src/test/resources/RequestBody/postUser.json"),
                UserDto.class);

        return user;
    }

    public static UserDto createAValidUser () throws IOException {
        UserDto validUser = createUser();

        return validUser;
    }

    public static UserDto createAnUnnamedUser () throws IOException {
        UserDto unnamedUser = createUser();
        unnamedUser.setName("");

        return unnamedUser;
    }

    public static UserDto createAnUserToGet(){

        UserDto user = new UserDto();

        user.setName("Eduardo");
        user.setJob("QA Analyst");

        return user;
    }

}
