package models;

import lombok.Data;

@Data
public class SuccessfulGetSingleUser {
    Data data;
    Support support;

    @lombok.Data
    public static class Data {
        int id;
        String email, first_name, last_name, avatar;
    }
    @lombok.Data
    public static class Support {
        String url, text;
    }

}