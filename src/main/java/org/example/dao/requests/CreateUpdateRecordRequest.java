package org.example.dao.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateRecordRequest {
    private String name;
    private String login;
    private String password;
    private String url;
}
