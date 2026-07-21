package com.brainz.sms_backend.Guardian;

import lombok.Data;

@Data
public class GuardianResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String relationship;
    private String occupation;

    public static GuardianResponse from(Guardian g) {
        GuardianResponse r = new GuardianResponse();
        r.setId(g.getId());
        r.setName(g.getName());
        r.setPhone(g.getPhone());
        r.setEmail(g.getEmail());
        r.setAddress(g.getAddress());
        r.setRelationship(g.getRelationship());
        r.setOccupation(g.getOccupation());
        return r;
    }
}
